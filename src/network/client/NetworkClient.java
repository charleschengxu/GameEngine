package network.client;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import network.exceptions.SessionExpiredException;
import network.core.Connection;
import network.core.ServerMode;
import network.exceptions.MessageCreationFailureException;
import network.exceptions.ServerDownException;
import network.messages.Message;
import network.messages.MessageType;
import network.messages.application.LockResponseMessage;

/** 
 * An implementation of of the {@link INetworkClient} interface.
 * 
 * All Communication channels are through TCP to ensure in-order 
 * and reliable delivery.
 * 
 * Please refer to {@link INetworkClient} for detailed API definition
 * and documents. Additional java document is included if necessary to
 * clarify its behavior given this particular implementation. 
 * 
 * @author CharlesXu
 */
public class NetworkClient implements INetworkClient {
	
	private static final long DELAY = 100;
	
	private Socket socket;
	private Connection connectionToServer;
	private BlockingQueue<Message> inComingBuffer;
	private Queue<Message> nonBlockingIncomingBuffer;
	private Multiplexer mux;
	private String userName;
	private long seqno;
	
	/**
	 * Default runs as development mode
	 * @param userName used to identify all messages sent by this client
	 * @throws ServerDownException if fail to connect to server
	 * 								or server not listening on <tt>serverPort</tt>
	 * 								or server rejects the connect request
	 */
	public NetworkClient(String userName) throws ServerDownException {
		this(userName, ServerMode.DEV);
	}
	
	/**
	 * Allows users to specify whether to run in dev mode or prod mode
	 * @param userName used to identify all messages sent by this client
	 * @param devMode a boolean that is true if to run in dev mode
	 * @throws ServerDownException if fail to connect to server
	 * 								or server not listening on <tt>serverPort</tt>
	 * 								or server rejects the connect request
	 */
	public NetworkClient(String userName, ServerMode mode)
			throws ServerDownException {
		this.userName = userName;
		try {
			socket = new Socket(mode.getServerName(), mode.getServerPort());
			inComingBuffer = new LinkedBlockingQueue<>();
			connectionToServer = new ConnectionToServer(
					inComingBuffer, socket, true, userName);
			nonBlockingIncomingBuffer = new LinkedList<>();
			mux = new Multiplexer();
			seqno = Long.MIN_VALUE;
			startReaderThread();
		} catch (IOException e) {
			throw new ServerDownException();
		}
	}
	
	/**
	 * Refers to {@link INetworkClient#read(MessageType type)} for more information. 
	 * 
	 * <p>This method is non-blocking. 
	 * 
	 * <p>Our developers are not familiar with the parallel computing paradigm
	 * in which a read from an empty synchronized buffer shall block the accessing
	 * thread. We have seen attempts to call this method using UI thread/main thread
	 * and the whole UI freezes when it blocks.
	 * 
	 * <p>To mitigate this challenge, the client starts a reader thread in its 
	 * constructor to implement this interface in a non-blocking fashion. 
	 * 
	 * @return a queue of messages read ordered in time
	 */
	@Override
	public Queue<Message> read(MessageType type) throws SessionExpiredException {
		if (connectionToServer.isClosed()) {
			throw new SessionExpiredException();
		}
		Queue<Message> msgsReceived;
		synchronized(nonBlockingIncomingBuffer) {
			msgsReceived = nonBlockingIncomingBuffer;
			nonBlockingIncomingBuffer = new LinkedList<>();
		}
		for(Message msg : msgsReceived) {
			msg.multiplex(mux);
		}
		Queue<Message> ret = mux.getMessageQueue(type);
		mux.flush(type);
		return ret;
	}
	
	/**
	 * Refers to {@link INetworkClient#broadcast(Object payload, MessageType type)}
	 */
	@Override
	public void broadcast(Object payload, MessageType type)
			throws MessageCreationFailureException, SessionExpiredException {
		if (connectionToServer.isClosed()) {
			throw new SessionExpiredException();
		}
		signAndsend(type, payload);
	}
	
	private void signAndsend(MessageType type, Object... payload)
			throws MessageCreationFailureException {
		Message msg = type.build(userName, payload);
		connectionToServer.send(msg);
	}
	
	/**
	 * Refers to {@link INetworkClient#disconnect()}
	 */
	@Override
	public void disconnect() {
		if (!connectionToServer.isClosed()) {
			try {
				signAndsend(MessageType.DISCONNECT);
				connectionToServer.close();
			} catch (MessageCreationFailureException e) {
				// trusted code with well defined message, no way for exception
			}
		}
	}
	
	/**
	 * This can NOT be extracted to another class. Passing the reference
	 * <tt>nonBlockingIncomingBuffer</tt> to another class (like in constructor)
	 * creates extra copy of reference to the same message queue. In that case, since
	 * NetworkClient.read() empties this queue by have the reference point to a new Queue,
	 * the extra copy in another class still points to old Queue. 
	 * 
	 * <p>One might propose a setter on that class to update its copy of the reference, but
	 * operations to change the two references are NOT atomic, which is vulnerable especially 
	 * when one copy is updated and the other is not. 
	 */
	private void startReaderThread() {
		Thread reader = new Thread() {
			@Override
			public void run() {
				while (!connectionToServer.isClosed()) {
					try {
						Message msg = inComingBuffer.take();
						synchronized(nonBlockingIncomingBuffer) {
							nonBlockingIncomingBuffer.add(msg);
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		reader.start();
	}

	/**
	 * This call is blocking until server has returned the assigned
	 * starting sequence number. Invoking this method gives the caller
	 * that lowest available sequence number and internally increments
	 * it by one to ensure no collision. Also thread safe.
	 * 
	 * <p>See also {@link INetworkClient#getNextSequenceNumber()}
	 */
	@Override
	public synchronized long getNextSequenceNumber() throws SessionExpiredException {
		if (seqno < 0) {
			seqno = connectionToServer.getStartingSequenceNumber();
		}
		return seqno++;
	}

	/**
	 * Refers to {@link INetworkClient#tryLock(long)}
	 * 
	 * <p> This call returns as soon as response from server is received.
	 * In the worst case when the server failed, it is blocked for as long
	 * as the remaining lease with the server. 
	 */
	@Override
	public String tryLock(long id) throws SessionExpiredException {
		signAndsend(MessageType.TRYLOCK, id);
		while (!connectionToServer.isClosed()) {
			Queue<Message> q = read(MessageType.LOCK_RESPONSE);
			if (q.isEmpty()){
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			} else {
				LockResponseMessage reponse = (LockResponseMessage) q.poll();
				return reponse.getPayload();
			}
		}
		throw new SessionExpiredException();
	}

	/**
	 * Refers to {@link INetworkClient#unlock(long)}
	 */
	@Override
	public void unlock(long id) throws SessionExpiredException {
		if (connectionToServer.isClosed()) {
			throw new SessionExpiredException();
		}
		signAndsend(MessageType.UNLOCK, id);
	}
	
	/**
	 * @return the client user name
	 */
	public String getUserName() {
		return userName;
	}
}
