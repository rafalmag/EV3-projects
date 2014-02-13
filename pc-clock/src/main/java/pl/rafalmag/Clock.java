package pl.rafalmag;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Note that this clock does not keep perfect time, but is close. It's main
 * purpose is to demonstrate various features of JavaFX.
 */
public class Clock extends Application {
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		// construct the analogueClock pieces.
		final Circle face = new Circle(100, 100, 100);
		face.setId("face");
		final Label brand = new Label("LEGO clock");
		brand.setId("brand");
		brand.layoutXProperty().bind(
				face.centerXProperty()
						.subtract(brand.widthProperty().divide(2)));
		brand.layoutYProperty().bind(
				face.centerYProperty().add(face.radiusProperty().divide(2)));
		final Line hourHand = new Line(0, 0, 0, -50);
		hourHand.setTranslateX(100);
		hourHand.setTranslateY(100);
		hourHand.setId("hourHand");
		final Line minuteHand = new Line(0, 0, 0, -75);
		minuteHand.setTranslateX(100);
		minuteHand.setTranslateY(100);
		minuteHand.setId("minuteHand");
		final Circle spindle = new Circle(100, 100, 5);
		spindle.setId("spindle");
		Group ticks = new Group();
		for (int i = 0; i < 12; i++) {
			Line tick = new Line(0, -83, 0, -93);
			tick.setTranslateX(100);
			tick.setTranslateY(100);
			tick.getStyleClass().add("tick");
			tick.getTransforms().add(new Rotate(i * (360 / 12)));
			ticks.getChildren().add(tick);
		}
		final Group analogueClock = new Group(face, brand, ticks, spindle,
				hourHand, minuteHand);

		// construct the digitalClock pieces.
		final Label digitalClock = new Label();
		digitalClock.setId("digitalClock");

		// determine the starting time.
		Calendar calendar = GregorianCalendar.getInstance();
		double seedMinuteDegrees = (calendar.get(Calendar.MINUTE)) * (360 / 60)
				+ calendar.get(Calendar.HOUR) * 360;

		// define rotations to map the analogueClock to the current time.
		final Rotate minuteRotate = new Rotate(seedMinuteDegrees);
		final Rotate hourRotate = new Rotate();
		hourRotate.angleProperty()
				.bind(minuteRotate.angleProperty().divide(12));
		hourHand.getTransforms().add(hourRotate);
		minuteHand.getTransforms().add(minuteRotate);

		initAnimation(digitalClock);

		stage.initStyle(StageStyle.TRANSPARENT);

		initMouseEvents(analogueClock, stage);

		initHand(hourHand, minuteRotate, 12);
		initHand(minuteHand, minuteRotate, 1);

		// layout the scene.
		final VBox layout = new VBox();
		layout.getChildren().addAll(analogueClock, digitalClock);
		layout.setAlignment(Pos.CENTER);
		final Scene scene = new Scene(layout, Color.TRANSPARENT);
		scene.getStylesheets().add(getResource("/clock.css"));
		stage.setScene(scene);

		// drag(stage, layout, scene);

		// show the scene.
		stage.show();
	}

	private void initHand(final Line hand, final Rotate minuteRotate,
			final double toMinuteMultiplier) {
		final DoubleProperty lastMinuteHandAngleValue = new SimpleDoubleProperty(
				0);
		hand.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				lastMinuteHandAngleValue
						.set(getMouseAngleRelativeToTarget(mouseEvent));
			}
		});
		hand.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				lastMinuteHandAngleValue
						.set(getMouseAngleRelativeToTarget(mouseEvent));
			}
		});
		hand.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boolean clockwiseRotation = isClockwiseRotation(
						lastMinuteHandAngleValue, mouseEvent);
				double addAngle = clockwiseRotation ? 6 : -6;
				rotateMinute(minuteRotate, addAngle * toMinuteMultiplier);
				// TODO use logger
				System.out.println("Minute rotation: "
						+ minuteRotate.getAngle()
						+ (clockwiseRotation ? " (+)" : " (-)"));
			}

		});
	}

	private void rotateMinute(final Rotate minuteRotate, double addAngle) {
		minuteRotate
				.setAngle((minuteRotate.getAngle() + addAngle) % (360 * 12));
	}

	private double getMouseAngleRelativeToTarget(MouseEvent mouseEvent) {
		Point2D centre = ((Node) mouseEvent.getSource())
				.localToScene(new Point2D(0, 0));

		double x = mouseEvent.getSceneX() - centre.getX();
		double y = mouseEvent.getSceneY() - centre.getY();
		double angleForCoordinates = CordsToAngle
				.getAngleForFxCoordinates(x, y);
		return angleForCoordinates;
	}

	private void initMouseEvents(final Group analogueClock, Stage stage) {
		// add a glow effect whenever the mouse is positioned over the clock.
		final Glow glow = new Glow();
		analogueClock.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				analogueClock.setEffect(glow);
			}
		});
		analogueClock.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				analogueClock.setEffect(null);
			}
		});

		// closeOnClick(analogueClock, stage);
	}

	private void closeOnClick(final Group analogueClock, final Stage stage) {
		// fade out the scene and shut it down when the mouse is clicked on the
		// clock.
		analogueClock.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				analogueClock.setMouseTransparent(true);
				FadeTransition fade = new FadeTransition(Duration.seconds(1.2),
						analogueClock);
				fade.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						stage.close();
					}
				});
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.play();
			}
		});
	}

	private void initAnimation(final Labeled digitalClock) {
		// the digital clock updates once a second.
		final Timeline digitalTime = new Timeline(new KeyFrame(
				Duration.seconds(0), new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						Calendar calendar = GregorianCalendar.getInstance();
						String hourString = pad(2, '0',
								calendar.get(Calendar.HOUR) == 0 ? "12"
										: calendar.get(Calendar.HOUR) + "");
						String minuteString = pad(2, '0',
								calendar.get(Calendar.MINUTE) + "");
						String secondString = pad(2, '0',
								calendar.get(Calendar.SECOND) + "");
						String ampmString = calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM"
								: "PM";
						digitalClock.setText(hourString + ":" + minuteString
								+ ":" + secondString + " " + ampmString);
					}
				}), new KeyFrame(Duration.seconds(1)));

		// time never ends.
		digitalTime.setCycleCount(Animation.INDEFINITE);

		// start the analogueClock.
		digitalTime.play();
	}

	private String pad(int fieldWidth, char padChar, String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length(); i < fieldWidth; i++) {
			sb.append(padChar);
		}
		sb.append(s);

		return sb.toString();
	}

	private boolean isClockwiseRotation(final DoubleProperty lastAngleValue,
			MouseEvent mouseEvent) {
		double angleForCoordinates = getMouseAngleRelativeToTarget(mouseEvent);
		double angleDiff = angleForCoordinates - lastAngleValue.get();
		lastAngleValue.set(angleForCoordinates);
		return angleDiff > 0;
	}

	static String getResource(String path) {
		return Clock.class.getResource(path).toExternalForm();
	}

}
