package authoring.controller.chat;

import java.util.Queue;

import authoring.share.NetworkController;
import authoring.view.chat.ChatView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import network.exceptions.MessageCreationFailureException;
import network.exceptions.SessionExpiredException;
import network.messages.Message;
import network.messages.MessageType;

public class ChatController {
	
	private ChatView myView;
	private NetworkController myNetworkController;
	
	public ChatController(NetworkController networkController) {
		myNetworkController = networkController;
	}

	public void init(ChatView view) {
		myView = view;
		KeyFrame frame = new KeyFrame(Duration.millis(100.0),
				new EventHandler<ActionEvent>() {
			@Override
			public void handle (ActionEvent event) {
				read();
			}
		});
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	public void send(String chatMessage) {
		try {
			myNetworkController.getClient().broadcast(chatMessage, MessageType.CHAT);
		} catch (MessageCreationFailureException | SessionExpiredException e) {
			e.printStackTrace();
		}
	}
	
	private void read() {
		if (myNetworkController.getClient() == null) return;
		try {
			Queue<Message> q = myNetworkController.getClient().read(MessageType.CHAT);
			while (!q.isEmpty()) {
				Message msg = q.poll();
				String chat = (String) msg.getPayload();
				myView.appendText(msg.getSender() + ": " + chat);
			}
		} catch (SessionExpiredException e) {
			System.out.println("session expired");
		}
	}
	
}
