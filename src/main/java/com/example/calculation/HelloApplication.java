package com.example.calculation;

import java.awt.MouseInfo;
import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.Node;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloApplication extends Application {
	static java.util.List<String> stringList = new ArrayList<>();
	private boolean chk51 = false;
	private boolean chk52 = false;
	private boolean chk1 = true;
	private boolean chk2 = true;
	private boolean chk3 = true;
	private boolean chk4 = true;
	private boolean chkAll = false;
	private boolean chk210 = false;
	private boolean chk211 = false;
	private boolean chk110 = false;
	private boolean chk0001 = false;
	private boolean chk0002 = false;
	private boolean chk0003 = false;
	private boolean chk0004 = false;
	private boolean sortDropDown = true;
	private boolean chk111 = false;
	private int rep = 1;
	TextArea textAreaD = new TextArea();
	Node node210;
	Node node211;
	java.util.List<Integer> rangeR = new ArrayList<>();;
	private String txt110 = "";
	private String txt111 = "";
	private int upperRange = 0;
	private int lowerRange = 0;
	private boolean otherFunc = false;
	private String drownDownHighLight = "";
	private boolean rangeDropDown = true;
	private String theText;
	private boolean isClicked = false;
	private Timeline timeline;
	private String txt51;
	private int previous;
	private String txt52;
	private String txt210 = "";
	private String txt211 = "";
	double visibleHeight;
	private boolean nextClick = false;
	private Timeline selectedTextTimeline;
	private String selectedText;
	private String unSelectedText;
	private String allText;
	private static int totalX;
	int pressed;

	private static java.util.List<int[]> totalCalculation = new ArrayList<>();
	private double pressX = 0;
	private double pressY = 0;
	private double cursorX = 0;
	private double cursorY = 0;
	private int rangeLimitFirst;
	int count = 0;

	private Rectangle selectionRectangle = new Rectangle();
	private java.util.List<Integer> range;
	private String highlightedFromExcel;
	private double[] ExcelcurrentCalculation;
	private double[] ExceltotalCalculation;
	private Boolean clicked_current = false;
	private Boolean clicked_total = false;
	private int excelOperation = 0;
	private String excelNumber;

	// For the Check Box for the randomized calculation you needed
	private boolean showResults = false;
	private boolean temp = false;

	public static void main(String[] args) {

		launch();
	}

	@Override
	public void start(Stage stage) throws IOException {

		totalX = 455;
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
		ScrollPane scrollPane = fxmlLoader.load();
		Pane pane = (Pane) scrollPane.getContent(); // Get the Pane from the ScrollPane
		Scene scene = new Scene(scrollPane, 1300, 600);

		// Check if the target of the event has the desired style class

		pane.setOnMousePressed(event -> {
			pressX = event.getX();
			pressY = event.getY();
			if (event.getTarget() instanceof Text) {
				Text clickedLabel = (Text) event.getTarget();
				if (clickedLabel.getText().equals("Calculated (Result's):")) {
					clicked_current = true;
					clicked_total = false;
					writeExcelSelectedDuplicate(pane, ExcelcurrentCalculation);

				} else if (clickedLabel.getText().equals("Calculated (Total's):")) {
					clicked_total = true;
					clicked_current = false;
					writeExcelSelectedDuplicate(pane, ExceltotalCalculation);

				}
			}
			if (event.isPrimaryButtonDown()) {
				pane.getChildren().remove(selectionRectangle);

			}

			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {

				Set<Node> textFields1 = pane.lookupAll("TextField");
				for (Node node : textFields1) {
					TextField textField1 = (TextField) node;
					if ("110".equals(textField1.getId()) && event.getX() < 650) {

						if (node.isVisible()) {
							sortDropDown = false;
						} else {
							sortDropDown = true;
						}
						node.setVisible(!node.isVisible());

					}
					if ("111".equals(textField1.getId()) && event.getX() < 650) {

						node.setVisible(!node.isVisible());

					}
					if ("210".equals(textField1.getId()) && event.getX() > 650) {
						if (node.isVisible()) {
							rangeDropDown = false;
							pane.getChildren().removeIf(node1 -> node1 instanceof Label
									&& node1.getStyleClass().contains("range-calculation"));
							pane.getChildren().removeIf(node1 -> node1 instanceof TextField
									&& node1.getStyleClass().contains("range-calculation"));
							pane.getChildren().removeIf(node1 -> node1 instanceof CheckBox
									&& node1.getStyleClass().contains("range-calculation"));

						} else {
							rangeDropDown = true;
						}
						node.setVisible(!node.isVisible());
					}
					if ("211".equals(textField1.getId()) && event.getX() > 650) {
						node.setVisible(!node.isVisible());
					}
				}
				Set<Node> checkBoxes = pane.lookupAll("CheckBox");
				for (Node node : checkBoxes) {
					CheckBox c = (CheckBox) node;
					if ("chk110".equals(c.getId()) && (int) event.getX() < 650) {
						node.setVisible(!node.isVisible());
					}
					if ("chk111".equals(c.getId()) && (int) event.getX() < 650) {
						node.setVisible(!node.isVisible());
					}
					if ("chk210".equals(c.getId()) && (int) event.getX() > 650) {
						node.setVisible(!node.isVisible());
					}
					if ("chk211".equals(c.getId()) && (int) event.getX() > 650) {
						node.setVisible(!node.isVisible());
					}
				}
			}

			// Clear previous selection rectangle
			// pane.getChildren().remove(selectionRectangle);
		});

		scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
			// Update the visible height whenever scrolling occurs
			visibleHeight = scrollPane.getViewportBounds().getHeight();
		});
		pane.setOnMouseDragged(event -> {

			java.awt.Point mousePosition = MouseInfo.getPointerInfo().getLocation();
			cursorX = mousePosition.getX();
			cursorY = mousePosition.getY();

			// Scroll the ScrollPane if the selection rectangle crosses the visible area
			visibleHeight = scrollPane.getViewportBounds().getHeight();
			double vValue = scrollPane.getVvalue();
			double selectionRectangleMaxY = cursorY;
			if (cursorY < 0) {
				vValue -= -cursorY / scrollPane.getContent().getBoundsInLocal().getHeight();
			} else if (selectionRectangleMaxY > visibleHeight) {
				// double vValue = (selectionRectangleMaxY - visibleHeight) / pane.getHeight();
				vValue += (cursorY - visibleHeight) / scrollPane.getContent().getBoundsInLocal().getHeight();
				double v = selectionRectangleMaxY / pane.getHeight();
				scrollPane.setVvalue(vValue);

			}

			double releaseX = event.getX();
			double releaseY = event.getY();

			double x1 = Math.min(pressX, releaseX);
			double y1 = Math.min(pressY, releaseY);
			double width = Math.abs(pressX - releaseX);
			double height = Math.abs(pressY - releaseY);
			double y2 = Math.max(y1, y1 + height);
			selectionRectangle.setX(x1);
			selectionRectangle.setY(y1);
			selectionRectangle.setWidth(width);
			selectionRectangle.setHeight(height);

			// Apply inline style for the selection rectangle
			selectionRectangle.setStyle("-fx-fill: #3399FF; -fx-opacity: 0.5;"); // Transparent blue color with opacity
			pane.getChildren().add(selectionRectangle);

		});
		ContextMenu contextMenu = new ContextMenu();
		MenuItem copyItem = new MenuItem("Copy");
		contextMenu.getItems().add(copyItem);
		pane.setOnMouseReleased(event -> {
			double releaseX = event.getX();
			double releaseY = event.getY();

		});
		// mouse events of selected rectangle
		selectionRectangle.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown()) {
				pane.getChildren().remove(selectionRectangle);

			}
			if (event.isSecondaryButtonDown()) {
				contextMenu.show(selectionRectangle, event.getScreenX(), event.getScreenY());
			}

			else {
				contextMenu.hide();
			}

		});
		java.util.List<String> list = new ArrayList<>();
		// copy the text from selected
		copyItem.setOnAction(e -> {
			// Get the bounds of the highlight rectangle
			Bounds bounds = selectionRectangle.getBoundsInParent();
			// Search for labels with "added-label" style class within the area covered by
			// the rectangle
			for (Node node : pane.getChildren()) {

				if (node instanceof Label) {
					Label label = (Label) node;
					if (label.getStyleClass().contains("highlighltlabel")
							&& bounds.intersects(label.getBoundsInParent())) {
						String labelText = label.getText();
						list.add(labelText);
						// Implement code to copy the labelText to clipboard or perform any other
						// action...
					}
				}
			}
			for (Node node : pane.getChildren()) {

				if (node instanceof Label) {
					Label label = (Label) node;
					if (label.getStyleClass().contains("current-calculation")
							&& bounds.intersects(label.getBoundsInParent())) {
						String labelText = label.getText();
						list.add(labelText);
						// Implement code to copy the labelText to clipboard or perform any other
						// action...
					}
				}
			}
			for (Node node : pane.getChildren()) {

				if (node instanceof Label) {
					Label label = (Label) node;
					if (label.getStyleClass().contains("current-count")
							&& bounds.intersects(label.getBoundsInParent())) {
						String labelText = label.getText();
						list.add(labelText);
						// Implement code to copy the labelText to clipboard or perform any other
						// action...
					}
				}
			}
			for (Node node : pane.getChildren()) {

				if (node instanceof Label) {
					Label label = (Label) node;
					if (label.getStyleClass().contains("added-label") && bounds.intersects(label.getBoundsInParent())) {
						String labelText = label.getText();
						list.add(labelText);
						// Implement code to copy the labelText to clipboard or perform any other
						// action...
					}
				}
			}
			for (Node node : pane.getChildren()) {

				if (node instanceof Label) {
					Label label = (Label) node;
					if (label.getStyleClass().contains("total-calculation")
							&& bounds.intersects(label.getBoundsInParent())) {
						String labelText = label.getText();
						list.add(labelText);
						// Implement code to copy the labelText to clipboard or perform any other
						// action...
					}
				}
			}

			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			StringBuilder stringBuilder = new StringBuilder();

			for (String item : list) {
				// stringBuilder.append(item).append("System.lineSeparator()");
				stringBuilder.append(item).append(" ");

			}

			content.putString(stringBuilder.toString());
			clipboard.setContent(content);
			list.clear();

		});

		pane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().toString().equals("ENTER")) {
				event.consume();
			}
		});
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

			if (event.getCode().toString().equals("ENTER")) {
				if (excelOperation != 0) {
					performExcelOperation(pane);
				}
				if (chk210 && rangeDropDown) {

					try {

						generateLimitRange(pane, rangeLimitFirst, chk110, otherFunc, nextClick, range, allText,
								selectedText, unSelectedText, chk51, chk52, previous);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}

				else if (chk51 && !chk52 && !chk110) {

					count++;
					for (int i = 0; i < rep; i++) {
						allText = txt51;
						MaketextParts();
						if (!unSelectedText.equals("")) {
							nextClick = true;
						}
						setLabels(pane, selectedText);
						try {

							int[][] a = generateCalculation(pane, otherFunc, nextClick, range, allText, selectedText,
									unSelectedText, chk51, chk52, previous);

						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}

				} else if (chk52 && !chk51 && !chk110) {
					otherFunc = true;
					count++;
					System.out.println("Chk 52 Clicked");
					for (int i = 0; i < rep; i++) {
						allText = txt52;
						MaketextParts();
						if (!unSelectedText.equals("")) {
							nextClick = true;
						}
						setLabels(pane, selectedText);

						try {

							int[][] a = generateCalculation(pane, otherFunc, nextClick, range, allText, selectedText,
									unSelectedText, chk51, chk52, previous);

						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}

				} else if (!chk52 && !chk51 && !chk110 && !txt110.equals("") && sortDropDown && !showResults) {
					otherFunc = true;
					count++;
					for (int i = 0; i < rep; i++) {
						allText = txt110;
						MaketextParts();
						if (!unSelectedText.equals("")) {
							nextClick = true;
						}
						setLabels(pane, selectedText);

						try {

							int[][] a = generateCalculation(pane, otherFunc, nextClick, range, allText, selectedText,
									unSelectedText, chk51, chk52, previous);

						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
				} else if (chk110 && sortDropDown) {
					String search = txt110;
					try {

						int[][] a = generateFilter(pane, search, chk110, otherFunc, nextClick, range, allText,
								selectedText, unSelectedText, chk51, chk52, previous);

					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				// For the Randomized Generation of the numbers
				else if (showResults && !chk51 && !chk52 && !chk110) {

					count++;
					for (int i = 0; i < rep; i++) {
						allText = txt51;

						MaketextParts();
						if (!unSelectedText.equals("")) {
							nextClick = true;
						}
						setLabelsForRandomizedCalculations(pane, selectedText);
						try {

							generateRandomizedCalculations(pane, otherFunc, nextClick, range, allText, selectedText,
									unSelectedText, chk51, chk52, previous);

						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}

				}

			}
		});
		// for two combo
		pane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().toString().equals("ENTER")) {

				if (chk51 && chk52) {
					setLabels(pane, selectedText);
					count++;
					try {
						Set<Node> textFields = pane.lookupAll("TextField");
						for (Node node : textFields) {
							TextField textField = (TextField) node;
							if (Integer.toString(previous).equals(textField.getId())) {
								unSelectedText = textField.getText();
								break;
							}
						}
						;
						if (!unSelectedText.equals("")) {
							nextClick = true;
						}

						int[][] a = generateCalculation(pane, otherFunc, nextClick, range, allText, selectedText,
								unSelectedText, chk51, chk52, previous);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

				}

			}
		});
		Set<Node> textFields = pane.lookupAll("TextField");
		for (Node node : textFields) {
			TextField textField = (TextField) node;
			if ("1".equals(textField.getId())) {

				// text selected from 5
				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					if (count == 0) {
						int[] change = convertToIntArray(textField.getText());
						Arrays.sort(change);
						String show = IntegerArrayToString(change);
						textField.setText(show);
					}
					txt51 = textField.getText();

				});
				textField.setOnMousePressed(event -> {

					isClicked = true;
					startTimer(textField.getText(), 2);

				});

				// Set the event handler for the mouse released event on the text field
				textField.setOnMouseReleased(event -> {
					isClicked = false;
					stopTimer();
				});

			}
			if ("2".equals(textField.getId())) {

				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					txt52 = textField.getText();

				});
				textField.setOnMousePressed(event -> {

					isClicked = true;
					startTimer(textField.getText(), 1);// previous is oopiste
				});

				// Set the event handler for the mouse released event on the text field
				textField.setOnMouseReleased(event -> {
					isClicked = false;
					stopTimer();
				});

			}
			if ("0".equals(textField.getId())) {
				textField.textProperty().addListener((observable, oldValue, newValue) -> {

					String rangeSte = textField.getText();
					theText = textField.getText();
					range = extractNumbersFromRange(rangeSte);

					rangeR = extractNumbersFromRangeForRandomized(rangeSte);
					System.out.println("Range: " + range.get(0) + " : " + range.get(1));

				});
			}
			if ("110".equals(textField.getId())) {
				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					if (count == 0) {
						int[] change = convertToIntArray(textField.getText());
						if (!chk0001) {
							Arrays.sort(change);
							String show = IntegerArrayToString(change);
							textField.setText(show);
						} else {
							showExcelNonPeriodLabel(pane, textField.getText());

						}

					}
					txt110 = textField.getText();

				});
			}
			if ("111".equals(textField.getId())) {
				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					int[] change = convertToIntArray(textField.getText());
					if (!chk0001) {
						Arrays.sort(change);
					} else {
						showExcelNonPeriodLabel(pane, textField.getText());
					}
					String show = IntegerArrayToString(change);
					textField.setText(show);
					txt111 = textField.getText();

				});
			}
			if ("210".equals(textField.getId())) {
				textField.textProperty().addListener((observable, oldValue, newValue) -> {

					txt210 = textField.getText();
					if (!chk210) {
						upperRange = convertToIntArray(txt210)[0];
					}

				});
			}
			if ("211".equals(textField.getId())) {
				textField.textProperty().addListener((observable, oldValue, newValue) -> {

					txt211 = textField.getText();
					if (!chk210) {
						lowerRange = convertToIntArray(txt211)[0];

					}

				});
			}
			if ("add-text".equals(textField.getId())) {

				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					excelNumber = textField.getText();

				});

			}
		}

		// check the checkboxes is it checked and store
		Set<Node> checkBoxes = pane.lookupAll("CheckBox");
		for (Node node : checkBoxes) {
			CheckBox c = (CheckBox) node;
			if ("chk51".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						chk51 = true;

					} else {
						chk51 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			if ("chk52".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk52 = true;

					} else {
						chk52 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk1".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk1 = true;

					} else {
						chk1 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk2".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk2 = true;

					} else {
						chk2 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk3".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk3 = true;

					} else {
						chk3 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk4".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk4 = true;

					} else {
						chk4 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk110".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk110 = true;
						Set<Node> textFieldshh = pane.lookupAll("TextField");
						for (Node nodehh : textFieldshh) {
							TextField textField = (TextField) nodehh;
							if ("110".equals(textField.getId())) {
								textField.setAlignment(Pos.CENTER);
							}
						}
						;

					} else {

						chk110 = false;
						Set<Node> textFieldshh = pane.lookupAll("TextField");
						for (Node nodehh : textFieldshh) {
							TextField textField = (TextField) nodehh;
							if ("110".equals(textField.getId())) {
								textField.setAlignment(Pos.CENTER_LEFT);

								textField.setText(drownDownHighLight);
							}
						}
						;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk111".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk111 = true;

					} else {
						chk111 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk210".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk210 = true;
						Set<Node> textFieldshh = pane.lookupAll("TextField");
						for (Node nodehh : textFieldshh) {
							TextField textField = (TextField) nodehh;
							if ("210".equals(textField.getId())) {
								textField.setAlignment(Pos.CENTER);
								break;
							}
						}
						;
						writeRange(pane);
						TextField txt = (TextField) node211;
						txt.setText("");

					} else {
						chk210 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("chk211".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk211 = true;

					} else {
						chk211 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("0001".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk0001 = true;
						ImportExcelFile(pane);
						showExcel();

					} else {
						chk0001 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("0002".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk0002 = true;

					} else {
						chk0002 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("0003".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk0003 = true;

					} else {
						chk0003 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("0004".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {
						chk0004 = true;

					} else {
						chk0004 = false;

					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});
			}
			if ("add-01".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						excelOperation = 1;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			if ("add-02".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						excelOperation = 2;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			if ("add-03".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						excelOperation = 3;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			if ("add-04".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						excelOperation = 4;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			if ("chkAll".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						chkAll = true;
					} else {
						chkAll = false;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			// For the CheckBox of Randomized Calculations
			if ("showResults".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						showResults = true;
					} else {
						showResults = false;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
			if ("temp".equals(c.getId())) {
				c.setOnAction(event -> {
					if (c.isSelected()) {

						temp = true;
					} else {
						temp = false;
					}
				});
				c.setOnKeyPressed(event -> {
					if (event.getCode().toString().equals("ENTER")) {
						event.consume();
					}
				});

			}
		}
		// check the checkboxes that are checked and store them in the boolean variables

		pane.setPrefHeight(1100);

		stage.setTitle("Calculator");
		stage.setScene(scene);
		stage.show();
	}

	private void showExcelNonPeriodLabel(Pane pane, String text) {
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("show-without-period"));

		int x = 80;
		int y = 50;
		String full = text;
		StringBuilder result = new StringBuilder();

		String[] full_arr = full.split(" ");
		java.util.List<Long> roundedList = new ArrayList<>();
		for (String str : full_arr) {
			double value = Double.parseDouble(str);
			long roundedValue = Math.round(value);
			roundedList.add(roundedValue);
		}
		for (Long sortedValue : roundedList) {
			result.append(sortedValue).append(" ");
		}
		if (result.length() > 0) {
			result.setLength(result.length() - 1);
		}
		result.append("          ");
		Collections.sort(roundedList);// sorting done
		for (Long sortedValue : roundedList) {
			result.append(sortedValue).append(" ");
		}
		if (result.length() > 0) {
			result.setLength(result.length() - 1);
		}

		labelWriteInExcel(pane, result.toString(), x, y, "show-without-period");

	}

	private void performExcelOperation(Pane pane) {
		totalX += 60;
		int y = 249;

		// Label labelTotal = new Label();
		// labelTotal.setWrapText(true); // Allow text to wrap within the label
		// labelTotal.setLayoutX(totalX);
		// labelTotal.setLayoutY(y);
		// labelTotal.setStyle("-fx-font-size: 12px;");
		// labelTotal.setTextFill(Color.rgb(40, 40, 43));
		// labelTotal.getStyleClass().add("total-calculation");
		// StringBuilder allLinesT= new StringBuilder();
		double[] selectedCal = ExceltotalCalculation;
		System.out.println("clickedCurrent: " + clicked_current + " ClickedTotal : " + clicked_total);
		if (clicked_current) {
			selectedCal = ExcelcurrentCalculation;
		} else if (clicked_total) {
			selectedCal = ExceltotalCalculation;
		}
		for (int i = 0; i < selectedCal.length; i++) {
			if (selectedCal[i] > 0) {
				if (excelOperation == 1) {
					selectedCal[i] += Integer.parseInt(excelNumber);
				} else if (excelOperation == 2) {
					selectedCal[i] -= Integer.parseInt(excelNumber);

				} else if (excelOperation == 3) {
					selectedCal[i] *= Integer.parseInt(excelNumber);

				} else if (excelOperation == 4) {
					selectedCal[i] /= Integer.parseInt(excelNumber);

				}

				// allLinesT.append( selectedCal[i]).append("\n");
				String c = Double.toString(selectedCal[i]);
				labelWriteInExcel(pane, c, totalX, y, "current-calculation");
				y = y + 20;

			}
		}
		// Text multilineTextT = new Text(allLinesT.toString());
		//
		// labelTotal.setGraphic(multilineTextT); // Set the Text as the label's graphic
		// pane.getChildren().add(labelTotal);
	}

	private void showExcel() {

	}

	private void ImportExcelFile(Pane pane) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			// Process the selected XLS file
			String filePath = file.getAbsolutePath();
			try (FileInputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis)) {
				Sheet sheet = workbook.getSheetAt(0);
				int rowCount = sheet.getPhysicalNumberOfRows();
				rowCount = 1000;
				ExcelcurrentCalculation = new double[rowCount];
				ExceltotalCalculation = new double[rowCount];

				for (int i = 0; i < rowCount; i++) {
					Row row = sheet.getRow(i);

					if (row != null) {
						Cell cell = row.getCell(0);
						if (cell != null) {
							String val = cell.getStringCellValue();
							if ((val.charAt(0)) == 'C') {

							} else if ((val.charAt(0)) == 'N') {
								int[] all = valueSperator(val);
								ExcelcurrentCalculation[all[0]] = all[1];
								ExceltotalCalculation[all[2]] = all[3];

							} else if (containsOnlyDigits(val)) {
								highlightedFromExcel = val;
							}

						}

					}
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println("Selected XLS file: " + filePath);
			displayExcelFile(pane);

		} else {
			// The user canceled the file selection
			System.out.println("No file selected.");
		}
	}

	private boolean containsOnlyDigits(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		Boolean sp = true;
		for (char ch : input.toCharArray()) {
			// Check if the character is not a space
			if (ch != ' ') {
				sp = false;
				break;
			}
		}
		for (char c : input.toCharArray()) {
			// Check if the character is not a digit
			if (!Character.isDigit(c) && c != ' ') {
				return false;
			}
		}
		if (!sp) {
			return true;
		}
		return false;

		// All characters are digits
	}

	private void labelWriteInExcel(Pane pane, String name, double x, double y, String classname) {
		Label labelCurrent = new Label();
		labelCurrent.setLayoutX(x);
		labelCurrent.setLayoutY(y);
		labelCurrent.setText(name);
		labelCurrent.getStyleClass().add(classname);
		pane.getChildren().add(labelCurrent);

	}

	private void writeExcelSelectedDuplicate(Pane pane, double[] calculation) {
		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("duplicate"));

		int y = 249;
		int step = 20;

		for (int i = 0; i < calculation.length; i++) {
			if (calculation[i] > 0) {

				String s = Integer.toString((int) calculation[i]);
				labelWriteInExcel(pane, s, totalX, y, "duplicate");
				y = y + step;
			}
		}
	}

	private void displayExcelFile(Pane pane) {
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-calculation"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("total-calculation"));

		int y = 249;
		int step = 20;

		int dynamicX = 310;
		pane.setPrefHeight(300000);
		pane.setPrefWidth(3000);

		double size = 0;
		double rows = 0;
		double maxCurrent = 0;
		for (int i = 0; i < ExcelcurrentCalculation.length; i++) {
			double occurance = (ExcelcurrentCalculation[i]);
			if (occurance > 0) {
				rows++;
				size = size + occurance;
				if (occurance > maxCurrent && i != 0) {
					maxCurrent = occurance;

				}

				String s = "Number of " + "'s = " + (int) occurance;
				labelWriteInExcel(pane, s, dynamicX, y, "current-calculation");

				y = y + step;
			}
		}

		setLabels(pane, highlightedFromExcel);
		y = 249;

		for (int i = 0; i < ExceltotalCalculation.length; i++) {
			if (ExceltotalCalculation[i] > 0) {

				String s = "(" + i + "),.... " + (int) ExceltotalCalculation[i];
				labelWriteInExcel(pane, s, totalX, y, "total-calculation");
				y = y + step;
			}
		}

		totalX += 140;
	}

	private int[] valueSperator(String input) {

		// Define a regular expression to match numbers
		Pattern pattern = Pattern.compile("\\b\\d+\\b");

		// Create a matcher with the input string
		Matcher matcher = pattern.matcher(input);

		// Find and print all matches
		int t = 0;
		int[] values = new int[4];
		while (matcher.find()) {
			int number = Integer.parseInt(matcher.group());
			values[t] = number;
			t++;
		}

		return values;
	}

	private String IntegerArrayToString(int[] change) {
		StringBuilder str = new StringBuilder();
		for (int i : change) {
			str.append(i).append(" ");
		}
		return str.toString();
	}

	private void writeRange(Pane pane) {
		Set<Node> textFields = pane.lookupAll("TextField");

		for (Node node : textFields) {
			TextField textField = (TextField) node;
			if ("210".equals(textField.getId())) {
				node210 = node;
				// rangeLimitFirst=customRound((float) upperRange /lowerRange);
				rangeLimitFirst = lowerRange;
				int rangeLimitSecond = upperRange;
				textField.setStyle("-fx-text-fill: red;" + "-fx-padding:1px;");

				textField.setText("(" + rangeLimitFirst + " To " + rangeLimitSecond + "),....");

			}
			if ("211".equals(textField.getId())) {
				node211 = node;
			}

		}

	}

	public static int customRound(float number) {
		float decimalPart = number - (int) number; // Extract decimal part
		if (decimalPart >= 0.5) {
			return (int) Math.ceil(number); // Round up
		} else {
			return (int) Math.floor(number); // Round down
		}
	}

	private void MaketextParts() {

		if (allText.contains("-")) {
			// If dash is present, split the input string into two parts
			String[] parts = allText.split("-");

			if (parts.length == 2) {

				selectedText = parts[0].trim();
				allText = selectedText;
				unSelectedText = parts[1].trim();
			}
		} else {
			selectedText = allText;
			unSelectedText = "";

		}

	}

	private void setLabels(Pane pane, String highlightedLabelText) {
		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("highlighltlabel"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("highlighltlabel-current"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("highlighltlabel-total"));

		Label lblcalculated = new Label("Calculated (Result's):");
		lblcalculated.setLayoutX(310);
		lblcalculated.setLayoutY(166);
		lblcalculated.setStyle("-fx-font-size: 12px;");
		lblcalculated.getStyleClass().add("highlighltlabel-current");

		Label lblTotalCalculated = new Label("Calculated (Total's):");
		lblTotalCalculated.setLayoutX(totalX);
		lblTotalCalculated.setLayoutY(166);
		lblTotalCalculated.setStyle("-fx-font-size: 12px;");
		lblTotalCalculated.getStyleClass().add("highlighltlabel-total");

		pane.getChildren().add(lblcalculated);
		pane.getChildren().add(lblTotalCalculated);
		int[] highlightedSort = convertToIntArray(highlightedLabelText);
		Arrays.sort(highlightedSort);
		StringBuilder str = new StringBuilder();
		for (int i : highlightedSort) {
			str.append(i).append(" ");
		}
		Label lblShow5data = new Label(str.toString());
		lblShow5data.setLayoutX(310);
		lblShow5data.setLayoutY(209);
		lblShow5data.setStyle("-fx-font-size: 12px;");
		lblShow5data.getStyleClass().add("highlighltlabel");

		pane.getChildren().add(lblShow5data);
	}

	private void handleEnterKeyPressed() {

	}

	public int[][] generateCalculation(Pane pane, boolean otherFunc, boolean nextClick, java.util.List<Integer> range,
			String allText, String selectedText, String unSelectedText, boolean chk51, boolean chk52, int previous)
			throws InterruptedException {
		int x = 310;

		boolean k = false;
		int step = 20;
		int y = 249;
		StringBuilder build = new StringBuilder();
		build.append(allText);
		build.append(" ");
		build.append(unSelectedText);
		allText = build.toString();

		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("added-label"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-calculation"));
		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-count"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("total-calculation"));
		int topPad = 183;
		int rowCountPad = 20;
		int sizePad = 20;
		int calculationPad = 20;
		int totalPad = rowCountPad + sizePad + calculationPad;

		int[] highlightednum = convertToIntArray(selectedText);
		Arrays.sort(highlightednum);
		int[][] HighlightedPairs = getNumberRepetitionPairs(highlightednum);

		int maxRepitition = findMostFrequentNumber(highlightednum);
		int[] unHighlighted = convertToIntArray(unSelectedText);

		int[] numbers = convertToIntArray(allText);
		Arrays.sort(numbers);
		int arrayLength = numbers.length;

		int result = findMultiplicationOfHighestAndSecondHighest(numbers);
		int highlightedResult = findMultiplicationOfHighestAndSecondHighest(highlightednum);
		if (highlightedResult < 99) {
			highlightedResult = 99;
		}
		if (highlightedResult > 999) {
			highlightedResult = 999;
		}
		// if(upperRange>0){highlightedResult=upperRange;}
		java.util.List<int[]> pairs;
		pairs = suitable_Allpairs(numbers);

		if (range == null) {
			range = new ArrayList<>();
			range.add(0);
			range.add(result);
		}

		// in totallpad the current and top are included
		int[][] currentCalculationList = new int[result + 1][2];
		ArrayList<String>[] currentBucket = new ArrayList[result + 1];

		// Initialize each bucket with an ArrayList
		for (int i = 0; i < currentBucket.length; i++) {
			currentBucket[i] = new ArrayList<>();
		}

		boolean run = true;
		if (nextClick) {
			run = true;
		}
		int dynamicX = 310;
		pane.setPrefHeight(300000);
		pane.setPrefWidth(3000);
		for (int i = 0; i <= highlightedResult; i++) {
			for (int[] pair : pairs) {

				int times = 1;
				if (chkAll) {
					times = 1;
				} else {
					// for pair seeing is it a highlighted started number
					for (int[] hPair : HighlightedPairs) {
						if (hPair[0] == pair[0]) {

							if (otherFunc && maxRepitition == hPair[0]) {
								times = hPair[1] * 10;
							} else {
								times = hPair[1] * 4;

							}

							break;

						}

					}
				}
				if (run && pair[1] != 0 && pair[0] % pair[1] == 0 && pair[0] >= pair[1] && chk4) {
					if (pair[0] / pair[1] == i) {
						for (int v = 0; v < times; v++) {
							String show = pair[0] + " / " + pair[1] + " = (" + i + "),....";

							if (chkAll || pair[0] == maxRepitition || pair[1] == maxRepitition || i == maxRepitition) {
								currentCalculationList[i][1] = currentCalculationList[i][1] + 1;
							}

							currentBucket[i].add(show);

						}

					}

				}
				if (run && pair[0] * pair[1] == i) {
					int chk = 3;
					if (pair[0] * pair[1] <= highlightedResult)
						for (int v = 0; v < times; v++) {
							int first = pair[0];
							int second = pair[1];
							int ans = i;
							String opt = " * ";
							if (nextClick && i > range.get(1)) {
								times = 1;
								if (i - pair[0] < range.get(1)) {
									chk = 2;
									first = i;
									second = pair[0];
									ans = i - pair[0];
									opt = " - ";
								} else if (i - pair[1] < range.get(1)) {
									chk = 2;

									first = i;
									second = pair[1];
									ans = i - pair[1];
									opt = " - ";
								} else if (pair[0] != 0) {
									if (i % pair[0] == 0) {
										chk = 4;

										first = i;
										second = pair[0];
										ans = i / pair[0];
										opt = " / ";
									}
								}

							}
							if ((chk == 3 && chk3) || (chk == 2 && chk2) || (chk == 4 && chk4)) {
								String show = first + opt + second + " = (" + ans + "),....";

								if (chkAll || first == maxRepitition || second == maxRepitition
										|| ans == maxRepitition) {
									currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
								}
								boolean high = false;
								for (Integer h : highlightednum) {
									if (first == h) {
										high = true;
										break;

									}
								}
								boolean add = true;
								if (!high) {
									for (String s : currentBucket[ans]) {
										if (show.equals(s)) {
											add = false;
											break;

										}
									}
								}
								if (add) {
									currentBucket[ans].add(show);
								}

							}

						}

				}
				if (run && pair[0] + pair[1] == i) {
					int ans = i;
					int chk = 1;
					if (pair[0] + pair[1] <= highlightedResult)
						for (int v = 0; v < times; v++) {

							int first = pair[0];
							int second = pair[1];
							String opt = " + ";
							if (false)
								if (nextClick && i > range.get(1)) {
									times = 1;
									if (pair[0] - i < range.get(1) && pair[0] - i > 0) {
										chk = 2;
										first = pair[0];
										second = i;
										ans = pair[0] - i;
										opt = " - ";
									} else if (i - pair[0] < range.get(1)) {
										chk = 2;

										first = i;
										second = pair[0];
										ans = i - pair[0];
										opt = " - ";
									} else if (i - pair[1] < range.get(1)) {
										chk = 2;
										first = i;
										second = pair[1];
										ans = i - pair[1];
										opt = " - ";
									} else if (pair[0] != 0)
										if (i % pair[0] == 0) {
											chk = 4;
											first = i;
											second = pair[0];
											ans = i / pair[0];
											opt = " / ";
										}

								}
							if ((chk == 1 && chk1) || (chk == 2 && chk2) || (chk == 4 && chk4)) {
								if (chkAll || first == maxRepitition || second == maxRepitition
										|| ans == maxRepitition) {
									currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
								}
								String show = first + opt + second + " = (" + ans + "),....";
								boolean high = false;
								for (Integer h : highlightednum) {
									if (first == h) {
										high = true;
										break;

									}
								}
								boolean add = true;
								if (!high) {
									for (String s : currentBucket[ans]) {
										if (show.equals(s)) {
											add = false;
											break;

										}
									}
								}
								if (add) {
									currentBucket[ans].add(show);
								}

							}

						}

				}
				if (pair[0] - pair[1] == i && chk2) {

					for (int v = 0; v < times; v++) {

						int first = pair[0];
						int second = pair[1];
						String opt = " - ";
						int ans = i;
						boolean sub1 = i - pair[0] > 0;
						boolean sub2 = i - pair[1] > 0;

						if (nextClick && i > range.get(1) && (sub1 || sub2)) {
							times = 1;
							if (sub1) {
								ans = i - pair[0];
								first = i;
								second = pair[0];
							} else {
								ans = i - pair[1];
								first = i;
								second = pair[1];
							}

							opt = " - ";

						}
						String show = first + opt + second + " = (" + ans + "),....";

						if (chkAll || first == maxRepitition || second == maxRepitition || ans == maxRepitition) {
							currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
						}
						boolean high = false;
						for (Integer h : highlightednum) {
							if (first == h) {
								high = true;
								break;
							}
						}
						boolean add = true;
						if (!high) {
							for (String s : currentBucket[ans]) {
								if (show.equals(s)) {
									add = false;
									break;

								}
							}
						}
						if (add) {
							currentBucket[ans].add(show);
						}

					}

				}

			}

		}
		int maxCurrentI = -9999;
		int maxCurrent = -99999;
		StringBuilder total = new StringBuilder();
		StringBuilder pre = new StringBuilder();
		total.append(selectedText);
		total.append("  -  ");
		int size = 0;
		int rows = 0;

		Label labelCurrent = new Label();
		labelCurrent.setWrapText(true); // Allow text to wrap within the label
		labelCurrent.setLayoutX(dynamicX);
		labelCurrent.setLayoutY(y);
		labelCurrent.setStyle("-fx-font-size: 12px;");
		labelCurrent.setTextFill(Color.rgb(40, 40, 43));
		labelCurrent.getStyleClass().add("current-calculation");
		StringBuilder allLinesC = new StringBuilder();
		for (int i = 0; i < currentCalculationList.length; i++) {
			int occurance = currentCalculationList[i][1];

			if (currentBucket[i].size() > 0) {
				rows++;
				size = size + occurance;
				addInTotal(i, occurance, totalCalculation);
				if (occurance > maxCurrent && i != 0) {
					maxCurrent = occurance;
					maxCurrentI = i;
				}
				total.append(Integer.toString(i));
				total.append(" ");
				pre.append(Integer.toString(i));
				pre.append(" ");

				allLinesC.append("Number of ").append(i).append("'s = ").append(occurance).append("\n");

				y = y + step;
			}
		}

		y += 10;

		allLinesC.append("\n").append("Row() Count: ").append(rows).append("\n\n");
		allLinesC.append("Size: ").append(size).append("\n\n");
		// noe displaying rowCount
		// y=displayRowCount(rows,size,y,25,pane);
		y += 30;

		for (int g = 0; g < currentBucket.length; g++) {
			int occ = currentBucket[g].size();
			currentBucket[g] = place_together_same_calculation(currentBucket[g]);
			for (int i = 0; i < occ; i++) {
				String show = currentBucket[g].get(i);
				if (i > 0 && !currentBucket[g].get(i - 1).equals(show)) {
					y += 10;
					allLinesC.append("\n");
				}
				allLinesC.append(show);
				allLinesC.append("\n");
				// createCalculationLabel(pane, show, dynamicX, y, "added-label");
				y += 20;
			}
			if (occ > 0) {
				y += 30;
				allLinesC.append("\n");
				allLinesC.append("\n\n");

			}
		}
		Text multilineTextC = new Text(allLinesC.toString());

		labelCurrent.setGraphic(multilineTextC); // Set the Text as the label's graphic
		pane.getChildren().add(labelCurrent);

		pane.setPrefHeight(y + 1000);

		displayCurrentMax3Field(maxCurrentI, pane);

		if (chk51 && !chk52) {
			Set<Node> textFields = pane.lookupAll("TextField");
			for (Node node : textFields) {
				TextField textField = (TextField) node;
				if ("1".equals(textField.getId())) {
					textField.setText(total.toString());
				}
			}
		} else if (chk52 && !chk51) {
			Set<Node> textFields = pane.lookupAll("TextField");
			for (Node node : textFields) {
				TextField textField = (TextField) node;
				if ("2".equals(textField.getId())) {
					textField.setText(total.toString());
				}
			}
		} else if (!chk52 && !chk51) {
			Set<Node> textFields = pane.lookupAll("TextField");
			for (Node node : textFields) {
				TextField textField = (TextField) node;
				if ("110".equals(textField.getId())) {
					textField.setText(total.toString());
					if (sortDropDown && otherFunc) {
						drownDownHighLight = total.toString();
					}
				}
			}
		} else if (chk52 && chk51) {

			Set<Node> textFields = pane.lookupAll("TextField");
			for (Node node : textFields) {
				TextField textField = (TextField) node;
				if (Integer.toString(previous).equals(textField.getId())) {
					textField.setText(pre.toString());
				}
			}

		}

		displyTotalMaxField(totalCalculation, pane);
		displyTotal(totalCalculation, pane, allText, selectedText, chk51, chk52, previous, result);

		return currentCalculationList;

	}

	private ArrayList<String> place_together_same_calculation(ArrayList<String> currentList) {
		Map<String, Integer> stringCounts = new HashMap<>();

		for (String str : currentList) {
			stringCounts.put(str, stringCounts.getOrDefault(str, 0) + 1);
		}

		currentList.clear();

		for (Map.Entry<String, Integer> entry : stringCounts.entrySet()) {
			String str = entry.getKey();
			int count = entry.getValue();

			for (int j = 0; j < count; j++) {
				currentList.add(str);
			}
		}

		return currentList;
	}

	private java.util.List<int[]> suitable_Allpairs(int[] numbers) {
		java.util.List<int[]> pairs = new ArrayList<>();

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers.length; j++) { // Start j from i + 1 to avoid duplicate pairs
				if (i != j) {
					int num1 = numbers[i];
					int num2 = numbers[j];

					int[] pair = { num1, num2 };
					pairs.add(pair);
				}
			}
		}

		return pairs;
	}

	public static void displyTotal(java.util.List<int[]> totalCalculation, Pane pane, String allText, String selected,
			boolean chk51, boolean chk52, int previous, int result) {

		int step = 20;
		int y = 249;
		Label labelTotal = new Label();
		labelTotal.setWrapText(true); // Allow text to wrap within the label
		labelTotal.setLayoutX(totalX);
		labelTotal.setLayoutY(y);
		labelTotal.setStyle("-fx-font-size: 12px;");
		labelTotal.setTextFill(Color.rgb(40, 40, 43));
		labelTotal.getStyleClass().add("total-calculation");
		StringBuilder allLinesT = new StringBuilder();
		for (int i = 0; i < result + 1; i++) {
			for (int[] array : totalCalculation) {
				if (array[0] == i) {

					// Label lblshow5data = new Label("(" + array[0] + "),.... " + array[1]);
					allLinesT.append("(").append(array[0]).append("),.... ").append(array[1]).append("\n");
					// lblshow5data.setLayoutX(totalX);
					// lblshow5data.setLayoutY(y);
					// lblshow5data.setStyle("-fx-font-size: 12px;");
					//
					// // Add a specific style class to the labels added in the loop
					// lblshow5data.getStyleClass().add("total-calculation");
					//
					// pane.getChildren().add(lblshow5data);

					// y = y + step;
					break;
				}
			}
		}
		Text multilineTextC = new Text(allLinesT.toString());

		labelTotal.setGraphic(multilineTextC); // Set the Text as the label's graphic
		pane.getChildren().add(labelTotal);

	}

	public static void FilterdisplyTotal(int[] preCheck, java.util.List<int[]> totalCalculation, Pane pane,
			String allText, String selected, boolean chk51, boolean chk52, int previous, int result) {

		int step = 20;
		int y = 249;
		Label labelTotal = new Label();
		labelTotal.setWrapText(true); // Allow text to wrap within the label
		labelTotal.setLayoutX(totalX);
		labelTotal.setLayoutY(y);
		labelTotal.setStyle("-fx-font-size: 12px;");
		labelTotal.setTextFill(Color.rgb(40, 40, 43));
		labelTotal.getStyleClass().add("total-calculation");

		StringBuilder allLinesT = new StringBuilder();
		for (int p : preCheck) {
			for (int[] array : totalCalculation) {
				if (array[1] != 0 && array[0] == p) {

					// Label lblshow5data = new Label("(" + array[0] + "),.... " + array[1]);
					// lblshow5data.setLayoutX(totalX);
					// lblshow5data.setLayoutY(y);
					// lblshow5data.setStyle("-fx-font-size: 12px;");
					allLinesT.append("(").append(array[0]).append("),.... ").append(array[1]).append("\n");
					//
					// // Add a specific style class to the labels added in the loop
					// lblshow5data.getStyleClass().add("total-calculation");
					//
					// pane.getChildren().add(lblshow5data);
					//
					// y = y + step;
					break;
				}

			}
		}
		Text multilineTextC = new Text(allLinesT.toString());

		labelTotal.setGraphic(multilineTextC); // Set the Text as the label's graphic
		pane.getChildren().add(labelTotal);

	}

	public static void addInTotal(int i, int occurance, java.util.List<int[]> totalCalculation) {
		for (int[] array : totalCalculation) {
			if (array[0] == i) {
				array[1] += occurance;
				return;
			}
		}
		int[] arr = { i, occurance };
		totalCalculation.add(arr);

	}

	public int displayRowCount(int RowCount, int size, int y, int next, Pane pane) {
		Label lblshow5data = new Label("Row() Count: " + RowCount);
		lblshow5data.setLayoutX(310);
		lblshow5data.setLayoutY(y);
		lblshow5data.setStyle("-fx-font-size: 12px;");
		// Add a specific style class to the labels added in the loop
		lblshow5data.getStyleClass().add("current-count");

		pane.getChildren().add(lblshow5data);
		y += next;

		Label lblshow5data1 = new Label("Size: " + size);
		lblshow5data1.setLayoutX(310);
		lblshow5data1.setLayoutY(y);
		lblshow5data1.setStyle("-fx-font-size: 12px;");

		lblshow5data1.getStyleClass().add("current-count");

		pane.getChildren().add(lblshow5data1);
		return y;

	}

	public static void displayCurrentMax3Field(int maxCurrent, Pane pane) {
		StringBuilder show = new StringBuilder();
		Set<Node> textFields = pane.lookupAll("TextField");
		for (Node node : textFields) {
			TextField textField = (TextField) node;
			if ("31".equals(textField.getId())) {
				String text3field = textField.getText();
				show.append(text3field);
				show.append(maxCurrent).append(" ");
				textField.setText(show.toString());

			}
		}
	}

	public static void displyTotalMaxField(java.util.List<int[]> totalCalculation, Pane pane) {
		int maxFirstElement = Integer.MIN_VALUE;
		int Max = 0;

		for (int i = 0; i < totalCalculation.size(); i++) {
			int[] array = totalCalculation.get(i);
			int firstElement = array[1];
			if (array[0] != 0) {
				if (firstElement > maxFirstElement) {
					maxFirstElement = firstElement;
					Max = array[0];
				}
			}
		}
		Set<Node> textFields = pane.lookupAll("TextField");
		for (Node node : textFields) {
			TextField textField = (TextField) node;
			if ("21".equals(textField.getId())) {
				textField.setText("");
				textField.setText(Integer.toString(Max));

			}
		}
	}

	public static void FilterdisplyTotalMaxField(int[] preCheck, java.util.List<int[]> totalCalculation, Pane pane) {
		int maxFirstElement = Integer.MIN_VALUE;
		int Max = 0;

		for (int i = 0; i < totalCalculation.size(); i++) {
			int[] array = totalCalculation.get(i);
			int firstElement = array[1];
			for (int p : preCheck) {
				if (array[0] != 0 && array[0] == p) {
					if (firstElement > maxFirstElement) {
						maxFirstElement = firstElement;
						Max = array[0];
					}
					break;
				}
			}
		}
		Set<Node> textFields = pane.lookupAll("TextField");
		for (Node node : textFields) {
			TextField textField = (TextField) node;
			if ("21".equals(textField.getId())) {
				textField.setText("");
				textField.setText(Integer.toString(Max));

			}
		}
	}

	public static int findMultiplicationOfHighestAndSecondHighest(int[] nums) {
		if (nums == null || nums.length < 2) {
			throw new IllegalArgumentException("Input array must have at least two elements.");
		}

		Arrays.sort(nums);
		int highest = nums[nums.length - 1];
		int secondHighest = nums[nums.length - 2];
		if (highest * secondHighest > highest + secondHighest) {
			return highest * secondHighest;
		} else {
			return highest + secondHighest;
		}

	}

	public static java.util.List<int[]> generate_uniquepairs(int[] nums) {
		java.util.List<int[]> pairs = new ArrayList<>();
		Set<Integer> addedPairs = new HashSet<>();

		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums.length; j++) {
				if (i != j) {
					int num1 = nums[i];
					int num2 = nums[j];
					int pairHashCode = (num1 * 31) + num2; // Create a unique hash code for the pair

					if (!addedPairs.contains(pairHashCode)) {
						int[] pair = { num1, num2 };
						pairs.add(pair);
						addedPairs.add(pairHashCode);
					}
				}
			}
		}

		return pairs;
	}

	public static java.util.List<int[]> suitable_uniquepairs(int[] nums) {
		java.util.List<int[]> pairs = new ArrayList<>();
		Set<String> addedPairs = new HashSet<>();
		boolean check = true;
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums.length; j++) { // Start j from i + 1 to avoid duplicate pairs
				if (i != j) {
					int num1 = nums[i];
					int num2 = nums[j];

					// Sort the pair to ensure (6, 7) and (7, 6) are considered the same
					int[] pair = { num1, num2 };
					for (int[] p : pairs) {
						if (p[0] == num1) {
							if (p[1] == num2) {
								check = false;
								break;
							}
						}
					}
					if (check) {
						pairs.add(pair);
					}
					check = true;

				}
			}
		}

		return pairs;
	}

	public static int[] convertToIntArray(String inputString) {
		String[] numberStrings = inputString.split(" ");
		int[] intArray = new int[numberStrings.length];

		for (int i = 0; i < numberStrings.length; i++) {
			try {
				intArray[i] = Integer.parseInt(numberStrings[i]);
			} catch (NumberFormatException e) {
				// If a non-integer value is encountered, you can choose to handle the exception
				// here.
				// In this example, we're simply skipping the invalid value and proceeding with
				// the rest.
				continue;
			}
		}

		return intArray;
	}

	public static int[][] getNumberRepetitionPairs(int[] arr) {
		// Use a HashMap to count occurrences of each number
		Map<Integer, Integer> numCounts = new HashMap<>();

		for (int num : arr) {
			numCounts.put(num, numCounts.getOrDefault(num, 0) + 1);
		}

		// Create the pair array based on the hashmap
		int[][] pairs = new int[numCounts.size()][2];
		int index = 0;

		for (Map.Entry<Integer, Integer> entry : numCounts.entrySet()) {
			pairs[index][0] = entry.getKey();
			pairs[index][1] = entry.getValue();
			index++;
		}

		return pairs;
	}

	private void startTimer(String allText, int previous) {

		stopTimer(); // Stop any previous running timer
		timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
			if (isClicked && chk52 && chk51) {

				this.allText = allText;
				this.previous = previous;
				MaketextParts();

			}
			stopTimer();
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private void stopTimer() {
		if (timeline != null) {
			timeline.stop();
			timeline = null;
		}
	}

	public static void createDynamicCalculationText(String show, TextArea textArea, int count) {
		textArea.appendText(show + "\n");
		textArea.setPrefRowCount(count);

	}

	public static void createCalculationLabel(Pane pane, String show, int dynamicX, int dynamixY, String className) {
		Label label = new Label(show);
		label.setLayoutX(dynamicX);
		label.setLayoutY(dynamixY);
		label.setStyle("-fx-font-size: 12px;");

		// Add a specific style class to the labels added in the loop
		label.getStyleClass().add(className);

		pane.getChildren().add(label);

	}

	private static String getSelectedText(String labelText, double pressX, double pressY, double releaseX,
			double releaseY) {
		// Calculate the selected text based on mouse press and release coordinates
		// For simplicity, this example assumes the whole label text is selected.
		return labelText;
	}

	public static java.util.List<Integer> extractNumbersFromRange(String rangeString) {
		java.util.List<Integer> numbersList = new ArrayList<>();

		String[] rangeParts = rangeString.split(" To ");
		System.out.println("RangeParts: " + rangeParts.length);
		if (rangeParts.length != 2) {
			throw new IllegalArgumentException("Invalid range format");
		}

		String startNumberS = rangeParts[0].substring(2).trim();
		int startNumber = Integer.parseInt(startNumberS);
		int endIndex = rangeParts[1].indexOf(")");

		int endNumber = Integer.parseInt(rangeParts[1].substring(0, endIndex).trim());

		numbersList.add(startNumber);

		numbersList.add(endNumber);

		return numbersList;
	}

	public static int findMostFrequentNumber(int[] arr) {
		Map<Integer, Integer> frequencyMap = new HashMap<>();

		for (int num : arr) {
			frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
		}

		int mostFrequentNumber = -1;
		int maxFrequency = 0;

		for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
			if (entry.getValue() > maxFrequency) {
				maxFrequency = entry.getValue();
				mostFrequentNumber = entry.getKey();
			}
		}

		return mostFrequentNumber;
	}

	public int[][] generateFilter(Pane pane, String search, boolean chk100, boolean otherFunc, boolean nextClick,
			java.util.List<Integer> range, String allText, String selectedText, String unSelectedText, boolean chk51,
			boolean chk52, int previous) throws InterruptedException {
		int x = 310;

		boolean k = false;
		int step = 20;
		int y = 249;
		StringBuilder build = new StringBuilder();
		build.append(allText);
		build.append(" ");
		build.append(unSelectedText);
		allText = build.toString();

		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("added-label"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-calculation"));
		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-count"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("total-calculation"));
		int topPad = 183;
		int rowCountPad = 20;
		int sizePad = 20;
		int calculationPad = 20;
		int totalPad = rowCountPad + sizePad + calculationPad;
		int searchNum = convertToIntArray(search)[0];

		int[] highlightednum = convertToIntArray(selectedText);
		Arrays.sort(highlightednum);
		int[][] HighlightedPairs = getNumberRepetitionPairs(highlightednum);

		int maxRepitition = findMostFrequentNumber(highlightednum);
		int[] unHighlighted = convertToIntArray(unSelectedText);

		int[] numbers = convertToIntArray(allText);
		Arrays.sort(numbers);
		int arrayLength = numbers.length;

		int result = findMultiplicationOfHighestAndSecondHighest(numbers);
		int highlightedResult = findMultiplicationOfHighestAndSecondHighest(highlightednum);
		if (highlightedResult < 99) {
			highlightedResult = 99;
		}
		if (highlightedResult > 999) {
			highlightedResult = 999;
		}
		// if(upperRange>0){highlightedResult=upperRange;}

		java.util.List<int[]> pairs;
		pairs = suitable_Allpairs(numbers);

		if (range == null) {
			range = new ArrayList<>();
			range.add(0);
			range.add(result);
		}

		// in totallpad the current and top are included
		int[][] currentCalculationList = new int[result + 1][2];
		ArrayList<String>[] currentBucket = new ArrayList[result + 1];

		// Initialize each bucket with an ArrayList
		for (int i = 0; i < currentBucket.length; i++) {
			currentBucket[i] = new ArrayList<>();
		}

		boolean run = true;
		if (nextClick) {
			run = true;
		}
		int dynamicX = 310;
		pane.setPrefHeight(300000);
		pane.setPrefWidth(3000);

		for (int i = 0; i <= highlightedResult; i++) {
			for (int[] pair : pairs) {
				int times = 1;
				// for pair seeing is it a highlighted started number
				if (chkAll) {
					times = 1;
				} else {
					for (int[] hPair : HighlightedPairs) {
						if (hPair[0] == pair[0]) {
							if (otherFunc && maxRepitition == hPair[0]) {
								times = hPair[1] * 10;
							} else {
								times = hPair[1] * 4;

							}

							if (chkAll) {
								times = 1;
							}
							break;

						}
						if (chkAll) {
							times = 1;
						}
					}
				}
				if (pair[0] == searchNum || pair[1] == searchNum || i == searchNum) {
					if (run && pair[1] != 0 && pair[0] % pair[1] == 0 && pair[0] >= pair[1] && chk4) {
						if (pair[0] / pair[1] == i) {
							for (int v = 0; v < times; v++) {
								String show = pair[0] + " / " + pair[1] + " = (" + i + "),....";

								if (chkAll || pair[0] == maxRepitition || pair[1] == maxRepitition
										|| i == maxRepitition) {
									currentCalculationList[i][1] = currentCalculationList[i][1] + 1;
								}
								currentBucket[i].add(show);

							}

						}

					}
					if (run && pair[0] * pair[1] == i) {
						int chk = 3;
						if (pair[0] * pair[1] <= highlightedResult)
							for (int v = 0; v < times; v++) {
								int first = pair[0];
								int second = pair[1];
								int ans = i;
								String opt = " * ";
								if (nextClick && i > range.get(1)) {
									times = 1;
									if (i - pair[0] < range.get(1)) {
										chk = 2;
										first = i;
										second = pair[0];
										ans = i - pair[0];
										opt = " - ";
									} else if (i - pair[1] < range.get(1)) {
										chk = 2;

										first = i;
										second = pair[1];
										ans = i - pair[1];
										opt = " - ";
									} else if (pair[0] != 0) {
										if (i % pair[0] == 0) {
											chk = 4;

											first = i;
											second = pair[0];
											ans = i / pair[0];
											opt = " / ";
										}
									}

								}
								if ((chk == 3 && chk3) || (chk == 2 && chk2) || (chk == 4 && chk4)) {
									String show = first + opt + second + " = (" + ans + "),....";

									if (chkAll || first == maxRepitition || second == maxRepitition
											|| ans == maxRepitition) {
										currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
									}
									boolean high = false;
									for (Integer h : highlightednum) {
										if (first == h) {
											high = true;
											break;

										}
									}
									boolean add = true;
									if (!high) {
										for (String s : currentBucket[ans]) {
											if (show.equals(s)) {
												add = false;
												break;

											}
										}
									}
									if (add) {
										currentBucket[ans].add(show);
									}

								}

							}

					}
					if (run && pair[0] + pair[1] == i) {
						int ans = i;
						int chk = 1;
						if (pair[0] + pair[1] <= highlightedResult)
							for (int v = 0; v < times; v++) {

								int first = pair[0];
								int second = pair[1];
								String opt = " + ";
								if (false)
									if (nextClick && i > range.get(1)) {
										times = 1;
										if (pair[0] - i < range.get(1) && pair[0] - i > 0) {
											chk = 2;
											first = pair[0];
											second = i;
											ans = pair[0] - i;
											opt = " - ";
										} else if (i - pair[0] < range.get(1)) {
											chk = 2;

											first = i;
											second = pair[0];
											ans = i - pair[0];
											opt = " - ";
										} else if (i - pair[1] < range.get(1)) {
											chk = 2;
											first = i;
											second = pair[1];
											ans = i - pair[1];
											opt = " - ";
										} else if (pair[0] != 0)
											if (i % pair[0] == 0) {
												chk = 4;
												first = i;
												second = pair[0];
												ans = i / pair[0];
												opt = " / ";
											}

									}
								if ((chk == 1 && chk1) || (chk == 2 && chk2) || (chk == 4 && chk4)) {
									if (chkAll || first == maxRepitition || second == maxRepitition
											|| ans == maxRepitition) {
										currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
									}
									String show = first + opt + second + " = (" + ans + "),....";
									boolean high = false;
									for (Integer h : highlightednum) {
										if (first == h) {
											high = true;
											break;

										}
									}
									boolean add = true;
									if (!high) {
										for (String s : currentBucket[ans]) {
											if (show.equals(s)) {
												add = false;
												break;

											}
										}
									}
									if (add) {
										currentBucket[ans].add(show);
									}

								}

							}

					}
					if (pair[0] - pair[1] == i && chk2) {

						for (int v = 0; v < times; v++) {

							int first = pair[0];
							int second = pair[1];
							String opt = " - ";
							int ans = i;
							boolean sub1 = i - pair[0] > 0;
							boolean sub2 = i - pair[1] > 0;

							if (nextClick && i > range.get(1) && (sub1 || sub2)) {
								times = 1;
								if (sub1) {
									ans = i - pair[0];
									first = i;
									second = pair[0];
								} else {
									ans = i - pair[1];
									first = i;
									second = pair[1];
								}

								opt = " - ";

							}
							String show = first + opt + second + " = (" + ans + "),....";

							if (chkAll || first == maxRepitition || second == maxRepitition || ans == maxRepitition) {
								currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
							}
							boolean high = false;
							for (Integer h : highlightednum) {
								if (first == h) {
									high = true;
									break;
								}
							}
							boolean add = true;
							if (!high) {
								for (String s : currentBucket[ans]) {
									if (show.equals(s)) {
										add = false;
										break;

									}
								}
							}
							if (add) {
								currentBucket[ans].add(show);
							}

						}

					}
				}

			}

		}
		int maxCurrentI = -9999;
		int maxCurrent = -99999;
		StringBuilder total = new StringBuilder();
		StringBuilder pre = new StringBuilder();
		total.append(selectedText);
		total.append("  -  ");
		int size = 0;
		int rows = 0;

		Label labelCurrent = new Label();
		labelCurrent.setWrapText(true); // Allow text to wrap within the label
		labelCurrent.setLayoutX(dynamicX);
		labelCurrent.setLayoutY(y);
		labelCurrent.setStyle("-fx-font-size: 12px;");
		labelCurrent.setTextFill(Color.rgb(40, 40, 43));
		labelCurrent.getStyleClass().add("current-calculation");
		StringBuilder allLinesC = new StringBuilder();
		for (int i = 0; i < currentCalculationList.length; i++) {
			int occurance = currentCalculationList[i][1];

			if (currentBucket[i].size() > 0) {
				rows++;
				size = size + occurance;
				// addInTotal(i,occurance,totalCalculation);
				if (occurance > maxCurrent && i != 0) {
					maxCurrent = occurance;
					maxCurrentI = i;
				}
				total.append(Integer.toString(i));
				total.append(" ");
				pre.append(Integer.toString(i));
				pre.append(" ");

				allLinesC.append("Number of ").append(i).append("'s = ").append(occurance).append("\n");

				y = y + step;
			}
		}

		y += 10;

		allLinesC.append("\n").append("Row() Count: ").append(rows).append("\n\n");
		allLinesC.append("Size: ").append(size).append("\n\n");
		// noe displaying rowCount
		// y=displayRowCount(rows,size,y,25,pane);
		y += 30;

		for (int g = 0; g < currentBucket.length; g++) {
			int occ = currentBucket[g].size();
			for (int i = 0; i < occ; i++) {
				String show = currentBucket[g].get(i);
				if (i > 0 && !currentBucket[g].get(i - 1).equals(show)) {
					y += 10;
					allLinesC.append("\n");
				}
				allLinesC.append(show);
				allLinesC.append("\n");
				// createCalculationLabel(pane, show, dynamicX, y, "added-label");
				y += 20;
			}
			if (occ > 0) {
				y += 30;
				allLinesC.append("\n");
				allLinesC.append("\n\n");

			}
		}
		Text multilineTextC = new Text(allLinesC.toString());

		labelCurrent.setGraphic(multilineTextC); // Set the Text as the label's graphic
		pane.getChildren().add(labelCurrent);

		pane.setPrefHeight(y + 1000);

		displayCurrentMax3Field(maxCurrentI, pane);
		if (chk100) {
			Set<Node> textFields = pane.lookupAll("TextField");
			for (Node node : textFields) {
				TextField textField = (TextField) node;
				if ("111".equals(textField.getId())) {
					textField.setText(pre.toString());
				}
			}
		}
		int[] preCheck = convertToIntArray(pre.toString());
		FilterdisplyTotalMaxField(preCheck, totalCalculation, pane);
		FilterdisplyTotal(preCheck, totalCalculation, pane, allText, selectedText, chk51, chk52, previous, result);

		return currentCalculationList;

	}

	public int[][] generateLimitRange(Pane pane, int startRange, boolean chk100, boolean otherFunc, boolean nextClick,
			java.util.List<Integer> range, String allText, String selectedText, String unSelectedText, boolean chk51,
			boolean chk52, int previous) throws InterruptedException {
		int x = 310;
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("range-calculation"));
		pane.getChildren()
				.removeIf(node -> node instanceof TextField && node.getStyleClass().contains("range-calculation"));
		pane.getChildren()
				.removeIf(node -> node instanceof CheckBox && node.getStyleClass().contains("range-calculation"));

		boolean k = false;
		int step = 20;
		int y = 249;
		StringBuilder build = new StringBuilder();
		build.append(allText);
		build.append(" ");
		build.append(unSelectedText);
		allText = build.toString();

		int topPad = 183;
		int rowCountPad = 20;
		int sizePad = 20;
		int calculationPad = 20;
		int totalPad = rowCountPad + sizePad + calculationPad;

		int[] highlightednum = convertToIntArray(selectedText);
		Arrays.sort(highlightednum);
		int[][] HighlightedPairs = getNumberRepetitionPairs(highlightednum);

		int maxRepitition = findMostFrequentNumber(highlightednum);
		int[] unHighlighted = convertToIntArray(unSelectedText);

		int[] numbers = convertToIntArray(allText);
		Arrays.sort(numbers);
		int arrayLength = numbers.length;

		int result = findMultiplicationOfHighestAndSecondHighest(numbers);
		int highlightedResult = findMultiplicationOfHighestAndSecondHighest(highlightednum);
		// if(upperRange>0){highlightedResult=upperRange;}

		java.util.List<int[]> pairs;
		pairs = suitable_Allpairs(numbers);

		if (range == null) {
			range = new ArrayList<>();
			range.add(0);
			range.add(result);
		}

		// in totallpad the current and top are included
		int[][] currentCalculationList = new int[highlightedResult + 1][2];
		ArrayList<String>[] currentBucket = new ArrayList[result + 1];

		// Initialize each bucket with an ArrayList
		for (int i = 0; i < currentBucket.length; i++) {
			currentBucket[i] = new ArrayList<>();
		}

		boolean run = true;
		if (nextClick) {
			run = true;
		}
		int dynamicX = 310;
		pane.setPrefHeight(300000);
		pane.setPrefWidth(3000);
		int getBack = range.get(1);
		for (int i = 0; i <= highlightedResult; i++) {
			for (int[] pair : pairs) {
				int times = 1;
				// for pair seeing is it a highlighted started number

				if (run && pair[1] != 0 && pair[0] % pair[1] == 0 && pair[0] >= pair[1] && chk4) {
					if (pair[0] / pair[1] == i && (pair[0] == maxRepitition || pair[1] == maxRepitition)) {
						for (int v = 0; v < times; v++) {
							String show = pair[0] + " / " + pair[1] + " = (" + i + "),....";

							currentCalculationList[i][1] = currentCalculationList[i][1] + 1;
							currentBucket[i].add(show);

						}

					}

				}
				if (run && pair[0] * pair[1] == i) {
					int chk = 3;
					if (pair[0] * pair[1] <= highlightedResult)
						for (int v = 0; v < times; v++) {
							int first = pair[0];
							int second = pair[1];
							int ans = i;
							String opt = " * ";
							if (nextClick && i > getBack) {
								times = 1;
								if (i - pair[0] < getBack) {
									chk = 2;
									first = i;
									second = pair[0];
									ans = i - pair[0];
									opt = " - ";
								} else if (i - pair[1] < getBack) {
									chk = 2;

									first = i;
									second = pair[1];
									ans = i - pair[1];
									opt = " - ";
								} else if (pair[0] != 0) {
									if (i % pair[0] == 0) {
										chk = 4;

										first = i;
										second = pair[0];
										ans = i / pair[0];
										opt = " / ";
									}
								}

							}
							if (first == maxRepitition || second == maxRepitition) {
								if ((chk == 3 && chk3) || (chk == 2 && chk2) || (chk == 4 && chk4)) {
									String show = first + opt + second + " = (" + ans + "),....";

									if (chkAll || first == maxRepitition || second == maxRepitition
											|| ans == maxRepitition) {
										currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
									}
									boolean high = false;
									for (Integer h : highlightednum) {
										if (first == h) {
											high = true;
											break;

										}
									}
									boolean add = true;
									if (!high) {
										for (String s : currentBucket[ans]) {
											if (show.equals(s)) {
												add = false;
												break;

											}
										}
									}
									if (add) {
										currentBucket[ans].add(show);
									}

								}
							}

						}

				}
				if (run && pair[0] + pair[1] == i) {
					int ans = i;
					int chk = 1;
					if (pair[0] + pair[1] <= highlightedResult)
						for (int v = 0; v < times; v++) {

							int first = pair[0];
							int second = pair[1];
							String opt = " + ";
							if (false)
								if (nextClick && i > getBack) {
									times = 1;
									if (pair[0] - i < getBack && pair[0] - i > 0) {
										chk = 2;
										first = pair[0];
										second = i;
										ans = pair[0] - i;
										opt = " - ";
									} else if (i - pair[0] < getBack) {
										chk = 2;

										first = i;
										second = pair[0];
										ans = i - pair[0];
										opt = " - ";
									} else if (i - pair[1] < getBack) {
										chk = 2;
										first = i;
										second = pair[1];
										ans = i - pair[1];
										opt = " - ";
									} else if (pair[0] != 0)
										if (i % pair[0] == 0) {
											chk = 4;
											first = i;
											second = pair[0];
											ans = i / pair[0];
											opt = " / ";
										}

								}
							if (first == maxRepitition || second == maxRepitition) {
								if ((chk == 1 && chk1) || (chk == 2 && chk2) || (chk == 4 && chk4)) {
									if (chkAll || first == maxRepitition || second == maxRepitition
											|| ans == maxRepitition) {
										currentCalculationList[ans][1] = currentCalculationList[ans][1] + 1;
									}
									String show = first + opt + second + " = (" + ans + "),....";
									boolean high = false;
									for (Integer h : highlightednum) {
										if (first == h) {
											high = true;
											break;

										}
									}
									boolean add = true;
									if (!high) {
										for (String s : currentBucket[ans]) {
											if (show.equals(s)) {
												add = false;
												break;

											}
										}
									}
									if (add) {
										currentBucket[ans].add(show);
									}

								}
							}

						}

				}
				if (pair[0] - pair[1] == i && chk2) {

					for (int v = 0; v < times; v++) {

						int first = pair[0];
						int second = pair[1];
						String opt = " - ";
						int ans = i;
						boolean sub1 = i - pair[0] > 0;
						boolean sub2 = i - pair[1] > 0;

						if (nextClick && i > getBack && (sub1 || sub2)) {
							times = 1;
							if (sub1) {
								ans = i - pair[0];
								first = i;
								second = pair[0];
							} else {
								ans = i - pair[1];
								first = i;
								second = pair[1];
							}

							opt = " - ";

						}
						if (first == maxRepitition || second == maxRepitition) {
							String show = first + opt + second + " = (" + ans + "),....";

							currentCalculationList[i][1] = currentCalculationList[i][1] + 1;
							boolean high = false;
							for (Integer h : highlightednum) {
								if (first == h) {
									high = true;
									break;
								}
							}
							boolean add = true;
							if (!high) {
								for (String s : currentBucket[ans]) {
									if (show.equals(s)) {
										add = false;
										break;

									}
								}
							}
							if (add) {
								currentBucket[ans].add(show);
							}
						}

					}

				}

			}

		}
		int rangeY = 166;
		StringBuilder answer = new StringBuilder();
		int endLoop = range.get(1);
		if (endLoop >= currentCalculationList.length) {
			endLoop = currentCalculationList.length - 1;
		}

		int maxCurrentI = -9999;
		int maxCurrent = -99999;

		Label labelCurrent = new Label();
		labelCurrent.setLayoutX(725);
		labelCurrent.setLayoutY(rangeY);

		labelCurrent.setTextFill(Color.rgb(160, 0, 0));

		labelCurrent.getStyleClass().add("range-calculation");
		labelCurrent.setText("(               &              ),....");
		pane.getChildren().add(labelCurrent);
		createRedLabel(pane, " Pattern's     Range's", 726, rangeY);

		java.util.List<int[]> matchList = new ArrayList<>();
		StringBuilder pattern = new StringBuilder();
		for (int g = endLoop; g >= startRange; g--) {
			stringList.clear();
			stringList.addAll(currentBucket[g]);
			boolean status = checkMatchingStrings();
			currentBucket[g].clear();
			currentBucket[g].addAll(stringList);

			if (status && currentBucket[g].size() % 2 == 0) {
				matchList.add(new int[] { g, 1 });

			}

		}

		TextField textField = new TextField();
		textField.setLayoutX(890); // Set x coordinate
		textField.setLayoutY(138); // Set y coordinate
		textField.setStyle("-fx-font-size: 12px;"); // Set font with size 12
		textField.setAlignment(Pos.CENTER);
		textField.setStyle("-fx-padding: 1;");
		textField.getStyleClass().add("range-calculation");
		pane.getChildren().add(textField);
		CheckBox chkMatch = new CheckBox();
		chkMatch.setLayoutX(1062);
		chkMatch.setLayoutY(138);
		chkMatch.setStyle("-fx-border-color: #318CE7; " + "-fx-border-width: 2px; " + "-fx-border-radius: 2px; "
				+ "-fx-border-style: solid; " + "-fx-background-color: transparent;" + "-fx-font-size: 9px;");
		chkMatch.getStyleClass().add("range-calculation");
		pane.getChildren().add(chkMatch);
		TextField textField1 = new TextField();
		textField1.setLayoutX(890); // Set x coordinate
		textField1.setLayoutY(158); // Set y coordinate
		textField1.setStyle("-fx-font-size: 12px;"); // Set font with size 12
		textField1.setStyle("-fx-padding: 1;");
		textField.setPrefHeight(21);
		textField.setPrefWidth(168);
		textField1.setPrefHeight(21);
		textField1.setPrefWidth(168);
		textField1.getStyleClass().add("range-calculation");
		pane.getChildren().add(textField1);
		if (matchList.size() > 0)
			textField.setText(String.valueOf(matchList.get(0)[0]));

		for (int[] fr : matchList) {
			for (int i = matchList.size() - 1; i >= 0; i--) {
				if (fr[0] == matchList.get(i)[0]) {

				}
				textField1.appendText(String.valueOf(matchList.get(i)[0]));
				textField1.appendText(" ");
			}
		}

		rangeY = rangeY + 40;

		int num = 1;
		for (int g = endLoop; g >= startRange; g--) {
			int occ = currentBucket[g].size();

			String match = "NM";
			int rangeX = 725;
			for (int[] matchL : matchList) {
				if (g == matchL[0]) {

					String cons = "";
					if (num == 1) {
						cons = "st";
					} else if (num == 2) {
						cons = "nd";
					} else if (num == 3) {
						cons = "rd";
					} else if (num == 4) {
						cons = "rth";
					} else if (num >= 5) {
						cons = "th";
					}
					match = String.valueOf(num) + cons;
					num++;
				}
			}

			if (occ > 0 && !match.equals("NM")) {
				createBlackLabel(pane, "(", rangeX, rangeY);
				rangeX += 3;
				if (matchList.size() > 0) {
					if (g == matchList.get(0)[0]) {
						createRedLabel(pane, String.valueOf(g), rangeX, rangeY);
					} else {
						createBlackLabel(pane, String.valueOf(g), rangeX, rangeY);
					}

				} else {
					createBlackLabel(pane, String.valueOf(g), rangeX, rangeY);
				}
				rangeX += 14;

				createBlackLabel(pane, "),.... Is (", rangeX, rangeY);
				rangeX += 36;
				if (!match.equals("NM"))
					createRedLabel(pane, match, rangeX, rangeY);
				else
					createBlackLabel(pane, match, rangeX, rangeY);
				rangeX += 20;
				StringBuilder middle = new StringBuilder();
				middle.append("),....     ").append(occ).append("     ");

				for (int i = 0; i < occ; i++) {
					String show = currentBucket[g].get(i);
					middle.append(show).append("   ");

				}
				Label l = createBlackLabel(pane, middle.toString(), rangeX, rangeY);
				rangeX += middle.toString().length() * 4;

				if (!match.equals("NM")) {
					createBlackLabel(pane, "(                            ),....", rangeX, rangeY);
					createRedLabel(pane, " Matching Pattern", rangeX + 1, rangeY);
				}
				rangeY += 30;

			}
		}
		rangeY += 10;
		for (int g = startRange; g <= endLoop; g++) {
			int occ = currentBucket[g].size();
			if (occ > 0) {
				pattern.append(g).append(" ");
			}
			String match = "NM";
			int rangeX = 725;

			for (int[] matchL : matchList) {
				if (g == matchL[0]) {
					match = "";
				}
			}

			if (occ > 0 && match.equals("NM")) {
				createBlackLabel(pane, "(", rangeX, rangeY);
				rangeX += 3;
				if (matchList.size() > 0) {
					if (g == matchList.get(0)[0]) {
						createRedLabel(pane, String.valueOf(g), rangeX, rangeY);
					} else {
						createBlackLabel(pane, String.valueOf(g), rangeX, rangeY);
					}

				} else {
					createBlackLabel(pane, String.valueOf(g), rangeX, rangeY);
				}
				rangeX += 14;

				createBlackLabel(pane, "),.... Is (", rangeX, rangeY);
				rangeX += 36;
				if (!match.equals("NM"))
					createRedLabel(pane, match, rangeX, rangeY);
				else
					createBlackLabel(pane, match, rangeX, rangeY);
				rangeX += 22;
				StringBuilder middle = new StringBuilder();
				middle.append("),....     ").append(occ).append("     ");

				for (int i = 0; i < occ; i++) {
					String show = currentBucket[g].get(i);
					middle.append(show).append("   ");

				}
				Label l = createBlackLabel(pane, middle.toString(), rangeX, rangeY);
				rangeX += middle.toString().length() * 4;

				rangeY += 30;

			}
		}
		if (!chk211) {
			TextField textField211 = (TextField) node210;
			textField211.setText(pattern.toString());
			return currentCalculationList;
		} else {
			TextField textField212 = (TextField) node211;
			textField212.setText(pattern.toString());
			textField212.setAlignment(Pos.CENTER_LEFT);

		}

		textField1.setText(pattern.toString());

		return currentCalculationList;
	}

	private Label createBlackLabel(Pane pane, String text, int x, int y) {
		Label label = new Label(text);
		label.setLayoutX(x);
		label.setLayoutY(y);
		label.getStyleClass().add("range-calculation");
		pane.getChildren().add(label);

		return label;
	}

	private Label createRedLabel(Pane pane, String text, int x, int y) {
		Label label = new Label(text);
		label.setLayoutX(x);
		label.setLayoutY(y);
		label.setWrapText(true); // Set wrap text to true
		label.setStyle("-fx-text-fill: red;");
		pane.getChildren().add(label);
		label.getStyleClass().add("range-calculation");

		return label;
	}

	public static boolean checkMatchingStrings() {
		java.util.List<String> answer = new ArrayList<>();
		if (stringList.size() == 0)
			return false;
		boolean br = true;
		for (int i = 0; i < stringList.size(); i++) {
			answer.add(stringList.get(i));
			String[] parts = stringList.get(i).split(" ");
			if (parts.length >= 3) {
				boolean firstCondition = false;
				int r = -1;
				boolean secondCondition = false;
				for (int j = 0; j < stringList.size(); j++) {
					if (j != i) {
						String[] otherParts = stringList.get(j).split(" ");
						if (parts[0].equals(otherParts[2]) && parts[2].equals(otherParts[0])) {
							firstCondition = true;
							r = j;
						}
						if (parts[2].equals(otherParts[2]) && parts[0].equals(otherParts[0])) {
							secondCondition = true;
							r = j;
						}
					}
				}
				if (r != -1 && (firstCondition || secondCondition)) {
					answer.add(stringList.get(r));
					stringList.remove(r);
				}
				if (!firstCondition && !secondCondition) {
					br = false;
				}
			}
		}
		stringList.clear();
		stringList.addAll(answer);
		return br;
	}

	// For the Generated of your randomized results

	public void generateRandomizedCalculations(Pane pane, boolean otherFunc, boolean nextClick, java.util.List<Integer> range,
			String allText, String selectedText, String unSelectedText, boolean chk51, boolean chk52, int previous)
			throws InterruptedException {

		int y = 224;

		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("added-label"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-calculation"));
		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("current-count"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("total-calculation"));

		int[] highlightednum = convertToIntArray(selectedText);
		Arrays.sort(highlightednum);

		int[] numbers = convertToIntArray(allText);
		Arrays.sort(numbers);

		// in totallpad the current and top are included

		boolean run = true;
		if (nextClick) {
			run = true;
		}
		int dynamicX = 310;
		pane.setPrefHeight(300000);
		pane.setPrefWidth(10000);
		System.out.println("All Text:" + allText);

		if (!allText.equals("")) {
			int[] arr = randomizedConvertToArray(allText);
			int minRange = 1;
			int maxRange = 30;

		
			// Default ranges
			java.util.List<Integer> numbersC = extractNumbersFromRangeForRandomized(theText);
			if (!numbersC.isEmpty()) {
				minRange = numbersC.get(0);
				maxRange = numbersC.get(1);

			}
			System.out.println("MinRange: "+minRange+" MaxRange: "+maxRange);
			int[][] permutations = generatePermutations(arr);
			Map<Integer, Set<String>> resultExpressions = generateAllPossibleCombinations(permutations, minRange,
					maxRange, chk1, chk2, chk3, chk4);
					Map<Integer, java.util.List<String>> sortedStrings = sorting(resultExpressions);
			Map<Integer, java.util.List<String>> pairedStrings = pairing(sortedStrings);

	        Map<Integer, java.util.List<String>> calculationsMap = OperationsCombinations.getOperationCalculations(arr, minRange, maxRange);

			
			StringBuilder formattedTextBuilder = new StringBuilder();
			StringBuilder total = new StringBuilder();
			total.append(selectedText);

			// y=y+(resultExpressions.size()*20);
			Label labelCurrent = new Label();
			labelCurrent.setWrapText(true); // Allow text to wrap within the label
			labelCurrent.setLayoutX(dynamicX);
			labelCurrent.setLayoutY(260);
			labelCurrent.setStyle("-fx-font-size: 12px;");
			labelCurrent.setTextFill(Color.rgb(40, 40, 43));
			labelCurrent.getStyleClass().add("current-calculation");
			for (Map.Entry<Integer, java.util.List<String>> entry : pairedStrings.entrySet()) {
				Integer key=entry.getKey();
				java.util.List<String> expressions = entry.getValue();
				int actualsize = 0;
				for (int i = 0; i < expressions.size(); i++) {
					String expression = expressions.get(i);

					y = y + 50;
					if (!tooSmall(expression)) {
						if (i != expressions.size() - 1) {
							if (expressionsAreEqual(expression, expressions.get(expressions.indexOf(expression) + 1))) {
								y = y + 100;
								formattedTextBuilder.append("\n\n").append(expression).append("\n\n")
										.append(expressions.get(i + 1)).append("\n\n");
								i++;
								actualsize += 2;
							} else {
								formattedTextBuilder.append(expression).append("\n\n\n\n");

							}
						}
					}

				}
				if (actualsize != 0) {
					formattedTextBuilder.append("\n\n");
				}


				
				

			}
			for (Map.Entry<Integer, java.util.List<String>> entry : pairedStrings.entrySet()) {
				java.util.List<String> expressions = entry.getValue();
				int actualsize = 0;
				for (int i = 0; i < expressions.size(); i++) {
					String expression = expressions.get(i);

				

					if (tooSmall(expression)) {
						if (i != expressions.size() - 1) {
							if (expressionsAreEqual(expression,
									expressions.get(expressions.indexOf(expression) + 1))) {
										y = y + 100;
								formattedTextBuilder.append("\n\n").append(expression).append("\n\n")
										.append(expressions.get(i + 1)).append("\n\n");
								i++;
								actualsize += 2;
							} else {
								formattedTextBuilder.append(expression).append("\n\n\n\n");

							}
						}
					}

				}
				if (actualsize != 0) {
					formattedTextBuilder.append("\n\n");
				}

			}
			for (Map.Entry<Integer, java.util.List<String>> entry : calculationsMap.entrySet()) {
				int result = entry.getKey();
				java.util.List<String> calculations = entry.getValue();
				Collections.sort(calculations, Comparator.comparingInt(OperationsCombinations::getFirstNumber));
			
				for (String calculation : calculations) {
					y = y + 50;
					formattedTextBuilder.append((calculation + " = ( " + result + " )")).append("\n\n\n");
				}
				formattedTextBuilder.append("\n\n");
			
			}

			formattedTextBuilder.append("\n\n");
			formattedTextBuilder.append("\n\n");
			// y = y + (pairedStrings.size() * 10000);
			// Create a Text node with the content of the StringBuilder
			Text multilineTextC = new Text(formattedTextBuilder.toString());

			labelCurrent.setGraphic(multilineTextC); // Set the Text as the label's graphic

			pane.getChildren().add(labelCurrent);

			displyTotalForRandomizedCalculations(pairedStrings, pane);

			pane.setPrefHeight(y + 1000);
		}

	}

	static class Operation {

		int operandIndex;
		char operator;
		int operand;

		Operation(int operandIndex, char operator, int operand) {
			this.operandIndex = operandIndex;
			this.operator = operator;
			this.operand = operand;
		}
	}
	  public static boolean tooSmallEach(String expression) {
	        int i = 0;
	        for (int j = 0; j < expression.length(); j++) {
	            if (expression.charAt(j) == '=') {
	                i++;
	            }

	        }
	        return i < 2 && (expression.contains("-") || expression.contains("/"));
	    }
	public static void generateCombinations(int[] arr, int index, int minRange, int maxRange, String expression,
			java.util.List<Operation> operations, Map<Integer, Set<String>> resultExpressions, Set<String> seenExpressions,
			boolean allowAddition, boolean allowSubtraction, boolean allowMultiplication, boolean allowDivision) {

		if (index == arr.length) {
			if (!bodMassRule(expression) || Same(expression)) {
				return;
			}

			int result = evaluateAndFormatExpression(expression).getResult();

			if (result >= minRange && result <= maxRange && !seenExpressions.contains(expression)
					&& containsOperator(expression)) {
				resultExpressions.computeIfAbsent(result, k -> new HashSet<>())
						.add(evaluateAndFormatExpression(expression).getFormattedExpression());
				seenExpressions.add(expression);
			}
			return;
		}

		int num = arr[index];

		// Include the current number
		if (allowAddition) {
			generateCombinations(arr, index + 1, minRange, maxRange,
					expression.isEmpty() ? String.valueOf(num) : expression + "+" + num, operations, resultExpressions,
					seenExpressions, true, allowSubtraction, allowMultiplication, allowDivision);
		}
		if (allowSubtraction) {
			generateCombinations(arr, index + 1, minRange, maxRange,
					expression.isEmpty() ? String.valueOf(num) : expression + "-" + num, operations, resultExpressions,
					seenExpressions, allowAddition, true, allowMultiplication, allowDivision);
		}
		if (allowMultiplication) {
			generateCombinations(arr, index + 1, minRange, maxRange,
					expression.isEmpty() ? String.valueOf(num) : expression + "*" + num, operations, resultExpressions,
					seenExpressions, allowAddition, allowSubtraction, true, allowDivision);
		}
		// Check if division is possible without fraction
		if (allowDivision && !expression.isEmpty() && !expression.endsWith("/") && num != 0
				&& evaluateExpression(expression) % num == 0) {
			generateCombinations(arr, index + 1, minRange, maxRange, expression + "/" + num, operations,
					resultExpressions, seenExpressions, allowAddition, allowSubtraction, allowMultiplication, true);
		}

		// Exclude the current number
		generateCombinations(arr, index + 1, minRange, maxRange, expression, operations, resultExpressions,
				seenExpressions, allowAddition, allowSubtraction, allowMultiplication, allowDivision);

		// Add current number as operation with previous result
		for (Operation operation : operations) {
			char operator = operation.operator;
			int operand = operation.operand;
			int result = evaluateExpression(expression + operator + operand);
			generateCombinations(arr, index + 1, minRange, maxRange, expression + "+" + result, operations,
					resultExpressions, seenExpressions, true, allowSubtraction, allowMultiplication, allowDivision);
			generateCombinations(arr, index + 1, minRange, maxRange, expression + operator + "(" + result + ")",
					operations, resultExpressions, seenExpressions, true, allowSubtraction, allowMultiplication,
					allowDivision);
		}

		// Add current number as new operation
		if (allowAddition) {
			operations.add(new Operation(index, '+', num));
		}
		if (allowSubtraction) {
			operations.add(new Operation(index, '-', num));
		}
		if (allowMultiplication) {
			operations.add(new Operation(index, '*', num));
		}
		if (allowDivision && num != 0) {
			operations.add(new Operation(index, '/', num));
		}

		// Remove current number as operation
		if (allowAddition) {
			operations.remove(operations.size() - 1);
		}
		if (allowSubtraction) {
			operations.remove(operations.size() - 1);
		}
		if (allowMultiplication) {
			operations.remove(operations.size() - 1);
		}
		if (allowDivision && num != 0) {
			operations.remove(operations.size() - 1);
		}
	}

	public static boolean containsOperator(String expression) {
		return expression.contains("+") || expression.contains("-") || expression.contains("*")
				|| expression.contains("/");
	}

	public static int evaluateExpression(String expression) {
		String[] tokens = expression.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
		int result = 0;
		try {
			result = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE; // Return a large value if expression is invalid
		}
		for (int i = 1; i < tokens.length; i += 2) {
			char operator = tokens[i].charAt(0);
			if (i + 1 < tokens.length) {
				try {
					int operand = Integer.parseInt(tokens[i + 1]);
					switch (operator) {
						case '+':
							result += operand;
							break;
						case '-':
							result -= operand;
							break;
						case '*':
							result *= operand;
							break;
						case '/':
							if (operand != 0) {
								result /= operand;
							}
							break;
					}
				} catch (NumberFormatException ignored) {
					return Integer.MAX_VALUE; // Return a large value if expression is invalid
				}
			}
		}
		return result;
	}

	public static int[][] generatePermutations(int[] originalArray) {
		int[][] permutations = new int[originalArray.length][originalArray.length];

		for (int i = 0; i < originalArray.length; i++) {
			permutations[i] = generateSinglePermutation(originalArray, i);
		}

		return permutations;
	}

	private static int[] generateSinglePermutation(int[] originalArray, int fixedIndex) {
		int[] permutation = Arrays.copyOf(originalArray, originalArray.length);

		// Swap the element at fixedIndex with the first element
		int temp = permutation[fixedIndex];
		permutation[fixedIndex] = permutation[0];
		permutation[0] = temp;

		return permutation;
	}

	public static void printPermutations(int[][] permutations) {
		for (int i = 0; i < permutations.length; i++) {
			System.out.print("{");
			for (int j = 0; j < permutations[i].length; j++) {
				if (j > 0) {
					System.out.print(",");
				}
				System.out.print(permutations[i][j]);
			}
			System.out.println("}");
		}
	}

	public static Map<Integer, Set<String>> generateAllPossibleCombinations(int[][] permutations, int minRange,
			int maxRange, boolean allowAddition, boolean allowSubtraction, boolean allowMultiplication,
			boolean allowDivision) {

		Map<Integer, Set<String>> individualResultExpressions = new HashMap<>();
		for (int[] individualArray : permutations) {
			generateCombinations(individualArray, 0, minRange, maxRange, "", new ArrayList<>(),
					individualResultExpressions, new HashSet<>(), true, true, true, true);
		}
		return individualResultExpressions;
	}

	public static Map<Integer, java.util.List<String>> pairing(Map<Integer, java.util.List<String>> formattedStrings) {
		Map<Integer, java.util.List<String>> pairedExpressions = new HashMap<>();

		for (Map.Entry<Integer, java.util.List<String>> entry : formattedStrings.entrySet()) {
			int result = entry.getKey();
			java.util.List<String> expressions = entry.getValue();
			java.util.List<String> paired = new ArrayList<>();

			for (String s : expressions) {
				if (!paired.contains(s)) {
					for (String j : expressions) {
						if (!paired.contains(j) && !paired.contains(s)) {
							if (expressionsAreEqual(s, j)) {
								paired.add(s);
								paired.add(j);

							}

						}

					}
				}

			}
			//

			pairedExpressions.put(result, paired);
		}

		return pairedExpressions;
	}

	private static boolean expressionsAreEqual(String expression1, String expression2) {
		if ((findFirstOperator(expression1) == '+' && findFirstOperator(expression2) == '+')
				|| (findFirstOperator(expression1) == '*' && findFirstOperator(expression2) == '*')) {

			if (findFirstOperator(expression1) == '+') {

				String[] expression1parts = expression1.split("\\+", 2);
				String[] expression1partss = expression1parts[1].split("\\=", 2);

				String[] expression2parts = expression2.split("\\+", 2);
				String[] expression2partss = expression2parts[1].split("\\=", 2);
				// System.out.println("expression1partss: "+expression1partss[1]+
				// "expression2partss: "+expression2partss[1] );
				if (expression1partss[1].equals(expression2partss[1])) {
					if (expression1parts[0].trim().equals(expression2partss[0].trim())
							&& expression1partss[0].trim().equals(expression2parts[0].trim())) {
						return true;
					}
				}

			} else {
				String[] expression1parts = expression1.split("\\*", 2);
				String[] expression1partss = expression1parts[1].split("\\=", 2);

				String[] expression2parts = expression2.split("\\*", 2);
				String[] expression2partss = expression2parts[1].split("\\=", 2);
				if (expression1partss[1].equals(expression2partss[1])) {
					if (expression1parts[0].trim().equals(expression2partss[0].trim())
							&& expression1partss[0].trim().equals(expression2parts[0].trim())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static char findFirstOperator(String expression) {
		int plusIndex = expression.indexOf("+");
		int minusIndex = expression.indexOf("-");
		int multiplyIndex = expression.indexOf("*");
		int divideIndex = expression.indexOf("/");

		int minIndex = Integer.MAX_VALUE;
		char firstOperator = '\0'; // Default value if no operator is found

		if (plusIndex != -1 && plusIndex < minIndex) {
			minIndex = plusIndex;
			firstOperator = '+';
		}
		if (minusIndex != -1 && minusIndex < minIndex) {
			minIndex = minusIndex;
			firstOperator = '-';
		}
		if (multiplyIndex != -1 && multiplyIndex < minIndex) {
			minIndex = multiplyIndex;
			firstOperator = '*';
		}
		if (divideIndex != -1 && divideIndex < minIndex) {
			minIndex = divideIndex;
			firstOperator = '/';
		}

		return firstOperator;
	}

	public static boolean bodMassRule(String expression) {
		// Remove all whitespace from the expression
		expression = expression.replaceAll("\\s+", "");

		// Initialize flags to track the presence of operators
		java.util.List<Character> array = new ArrayList<>();
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '+' || expression.charAt(i) == '*' || expression.charAt(i) == '-'
					|| expression.charAt(i) == '/') {
				array.add(expression.charAt(i));
			}
		}
		java.util.List<Integer> additions = new ArrayList<>();
		java.util.List<Integer> subtractions = new ArrayList<>();
		java.util.List<Integer> multiplications = new ArrayList<>();
		java.util.List<Integer> divisions = new ArrayList<>();

		for (int i = 0; i < array.size(); i++) {
			Character c = array.get(i);
			// System.out.println(c + " " + i);
			if (c == '+') {
				additions.add(i);
			} else if (c == '-') {
				subtractions.add(i);
			} else if (c == '*') {
				multiplications.add(i);
			} else if (c == '/') {
				divisions.add(i);
			}
		}
		int admax = 2;
		int submax = 3;
		int mulmax = 1;
		int divmax = 0;

		int admin = 2;
		int submin = 3;
		int mulmin = 1;
		int divmin = 0;
		if (!additions.isEmpty()) {
			admax = Collections.max(additions);
			admin = Collections.min(additions);
		}
		if (!subtractions.isEmpty()) {
			submax = Collections.max(subtractions);
			submin = Collections.min(subtractions);
		}
		if (!multiplications.isEmpty()) {
			mulmin = Collections.min(multiplications);
			mulmax = Collections.max(multiplications);
		}
		if (!divisions.isEmpty()) {

			divmax = Collections.max(divisions);
			divmin = Collections.min(divisions);
		}

		if (divisions.size() > 1 || multiplications.size() > 1) {
			return false;
		}
		if (!divisions.isEmpty()) {
			if (divmax > submin || divmax > mulmin || divmax > admin) {

				return false;
			}
		}

		if (!multiplications.isEmpty()) {
			if (mulmax > admin || mulmax > submin) {
				return false;
			}
		}
		if (!additions.isEmpty()) {
			if (admax > submin) {
				return false;
			}
		}
		// If no addition or subtraction is found, the rule is violated
		return true;
	}

	public static boolean Same(String expression) {

		// Define a regular expression pattern to match numbers
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

		// Create a Matcher object
		Matcher matcher = pattern.matcher(expression);

		// Create an ArrayList to store the numbers
		ArrayList<Integer> numbers = new ArrayList<>();

		// Find and add all matches to the ArrayList
		while (matcher.find()) {
			numbers.add(Integer.parseInt(matcher.group())); // Convert the string to an integer
		}

		// Convert ArrayList to an array if needed
		Integer[] numbersArray = numbers.toArray(new Integer[0]);

		// Print the array
		if (!findDuplicates(numbersArray)) {
			return false;
		}
		return true;
	}

	public static boolean findDuplicates(Integer[] array) {
		HashSet<Integer> set = new HashSet<>();
		HashSet<Integer> duplicates = new HashSet<>();

		for (int i = 0; i < array.length; i++) {
			if (!set.add(array[i])) { // If the element already exists in the set, it's a duplicate
				return true;
			}
		}

		return false;
	}

	public static boolean tooSmall(String expression) {
		int i = 0;
		for (int j = 0; j < expression.length(); j++) {
			if (expression.charAt(j) == '=') {
				i++;
			}

		}
		return i < 2 && (expression.contains("+") || expression.contains("*"));
	}

	public static ExpressionEvaluationResult evaluateAndFormatExpression(String expression) {
		String[] tokens = expression.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
		int result = 0;
		StringBuilder formattedExpression = new StringBuilder();
		try {
			result = Integer.parseInt(tokens[0]);
			formattedExpression.append(tokens[0]);
		} catch (NumberFormatException e) {
			return new ExpressionEvaluationResult(Integer.MAX_VALUE, ""); // Return a large value if expression is
																			// invalid
		}
		for (int i = 1; i < tokens.length; i += 2) {
			char operator = tokens[i].charAt(0);
			if (i + 1 < tokens.length) {
				try {
					int operand = Integer.parseInt(tokens[i + 1]);
					switch (operator) {
						case '+':
							result += operand;
							break;
						case '-':
							result -= operand;
							break;
						case '*':
							result *= operand;
							break;
						case '/':
							if (operand != 0) {
								result /= operand;
							}
							break;
					}
					formattedExpression.append(" ").append(operator).append(" ").append(operand).append(" = ( ")
							.append(result).append(" )");
				} catch (NumberFormatException ignored) {
					return new ExpressionEvaluationResult(Integer.MAX_VALUE, ""); // Return a large value if expression
																					// is invalid
				}
			}
		}
		return new ExpressionEvaluationResult(result, formattedExpression.toString());
	}

	public static class ExpressionEvaluationResult {

		private int result;
		private String formattedExpression;

		public ExpressionEvaluationResult(int result, String formattedExpression) {
			this.result = result;
			this.formattedExpression = formattedExpression;
		}

		public int getResult() {
			return result;
		}

		public String getFormattedExpression() {
			return formattedExpression;
		}
	}

	public static boolean noPairing(String expression) {
		java.util.List<Character> characterList = new ArrayList<>();
		if (!expression.isEmpty()) {
			for (int i = 0; i < expression.length(); i++) {
				char c = expression.charAt(i);
				if (c == '+' || c == '-' || c == '*' || c == '/') {
					characterList.add(c);
				}
			}
			if (!characterList.isEmpty()) {
				return characterList.get(0) == '/' || characterList.get(0) == '-';
			}
		}
		return false;
	}

	public static Map<Integer, Integer> getListofResults(Map<Integer, java.util.List<String>> resultExpressions) {
		Map<Integer, Integer> newList = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, java.util.List<String>> entry : resultExpressions.entrySet()) {
			int number = entry.getKey();
			java.util.List<String> combinations = entry.getValue();
			newList.put(number, combinations.size());
		}

		java.util.List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(newList.entrySet());

		// Sort the list by values (the second integers) in descending order
		entryList.sort(Map.Entry.<Integer, Integer>comparingByKey());

		// Create a new LinkedHashMap to maintain the order of sorted entries
		Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<Integer, Integer> entry : entryList) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static int[] randomizedConvertToArray(String inputString) {
		// Split inputString by one or more spaces
		String[] numberStrings = inputString.split("\\s+");

		// Use a HashSet to automatically remove duplicates
		HashSet<Integer> numberSet = new HashSet<>();

		for (String numStr : numberStrings) {
			try {
				int number = Integer.parseInt(numStr);
				numberSet.add(number);
			} catch (NumberFormatException e) {
				// If a non-integer value is encountered, you can choose to handle the exception
				// here.
				// In this example, we're simply skipping the invalid value and proceeding with
				// the rest.
				continue;
			}
		}

		// Convert HashSet back to int array
		int[] intArray = new int[numberSet.size()];
		int index = 0;
		for (int num : numberSet) {
			intArray[index++] = num;
		}

		return intArray;
	}

	public static void displyTotalForRandomizedCalculations(Map<Integer, java.util.List<String>> resultExpressions, Pane pane) {

		Label labelTotal = new Label();
		labelTotal.setWrapText(true); // Allow text to wrap within the label
		labelTotal.setLayoutX(310);
		labelTotal.setLayoutY(214);
		labelTotal.setStyle("-fx-font-size: 12px;");
		labelTotal.setTextFill(Color.rgb(40, 40, 43));
		labelTotal.getStyleClass().add("total-calculation");

		Label verticalTotal = new Label();
		verticalTotal.setWrapText(true); // Allow text to wrap within the label
		verticalTotal.setLayoutX(1000);
		verticalTotal.setLayoutY(214);
		verticalTotal.setStyle("-fx-font-size: 12px;");
		verticalTotal.setTextFill(Color.rgb(40, 40, 43));
		verticalTotal.getStyleClass().add("total-calculation");

		StringBuilder allLinesT = new StringBuilder();
		StringBuilder allLinesVertical = new StringBuilder();

		allLinesT.append("(");
		Map<Integer, Integer> list = getListofResults(resultExpressions);
		int i = 0;
		for (Map.Entry<Integer, Integer> entry : list.entrySet()) {
			Integer number = entry.getKey();
			Integer combinations = entry.getValue();
			i++;
			if (combinations != 0) {

				allLinesT.append("  ").append(number);
				allLinesVertical.append("( ").append(number).append(" ),....  ").append(combinations).append("\n");
			}
			if (i % 20 == 0) {
				allLinesT.append("\n");
			}

		}
		allLinesT.append(" ),...");

		Text multilineTextC = new Text(allLinesT.toString());
		Text multilineTextVertical = new Text(allLinesVertical.toString());

		labelTotal.setGraphic(multilineTextC); // Set the Text as the label's graphic
		verticalTotal.setGraphic(multilineTextVertical); // Set the Text as the label's graphic
		pane.getChildren().add(labelTotal);
		pane.getChildren().add(verticalTotal);

	}

	private void setLabelsForRandomizedCalculations(Pane pane, String highlightedLabelText) {
		pane.getChildren().removeIf(node -> node instanceof Label && node.getStyleClass().contains("highlighltlabel"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("highlighltlabel-current"));
		pane.getChildren()
				.removeIf(node -> node instanceof Label && node.getStyleClass().contains("highlighltlabel-total"));

		int[] highlightedSort = convertToIntArray(highlightedLabelText);
		Arrays.sort(highlightedSort);
		StringBuilder str = new StringBuilder();
		for (int i : highlightedSort) {
			str.append(i).append(" ");
		}
		Label lblShow5data = new Label(str.toString());
		lblShow5data.setLayoutX(310);
		lblShow5data.setLayoutY(166.0);
		lblShow5data.setStyle("-fx-font-size: 12px;");
		lblShow5data.getStyleClass().add("highlighltlabel");

		pane.getChildren().add(lblShow5data);
	}

	public static java.util.List<Integer> extractNumbersFromRangeForRandomized(String rangeString) {
		java.util.List<Integer> numbersList = new ArrayList<>();

		String[] rangeParts = rangeString.split(" To ");
		System.out.println("RangeParts: " + rangeParts.length);
		if (rangeParts.length != 2) {
			throw new IllegalArgumentException("Invalid range format");
		}

		String startNumberS = rangeParts[0].substring(2).trim();
		int startNumber = Integer.parseInt(startNumberS);
		int endIndex = rangeParts[1].indexOf(")");

		int endNumber = Integer.parseInt(rangeParts[1].substring(0, endIndex).trim());

		numbersList.add(startNumber);

		numbersList.add(endNumber);

		return numbersList;
	}

	  public static Map<Integer, java.util.List<String>> sorting(Map<Integer, Set<String>> resultExpressions) {
        Map<Integer, java.util.List<String>> newList = new HashMap<>();
        for (Map.Entry<Integer, Set<String>> entry : resultExpressions.entrySet()) {
            int resultIndividual = entry.getKey();
            Set<String> expressions = entry.getValue();
            java.util.List<String> myList = new ArrayList<>(expressions);
            Collections.sort(myList);
            newList.put(resultIndividual, myList);

        }
        return newList;
    }
	  

public class OperationsCombinations {

  

    public static Map<Integer, java.util.List<String>> getOperationCalculations(int[] array, int minResult, int maxResult) {
        Map<Integer, java.util.List<String>> calculationsMap = new HashMap<>();
        
        // Generate all combinations of two numbers from the array
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                int num1 = array[i];
                int num2 = array[j];
                calculateOperations(calculationsMap, num1, num2, minResult, maxResult);
            }
        }
        
        return calculationsMap;
    }

    // Method to calculate results of all four operations between two numbers
    public static void calculateOperations(Map<Integer, java.util.List<String>> calculationsMap, int num1, int num2,
            int minResult, int maxResult) {
        // Apply addition
        int addition = num1 + num2;
        addCalculation(calculationsMap, addition, num1 + " + " + num2,minResult,maxResult);

        // Apply subtraction
        int subtraction1 = num1 - num2;
        addCalculation(calculationsMap, subtraction1, num1 + " - " + num2,minResult,maxResult);
        int subtraction2 = num2 - num1;
        addCalculation(calculationsMap, subtraction2, num2 + " - " + num1,minResult,maxResult);

        // Apply multiplication
        int multiplication = num1 * num2;
        addCalculation(calculationsMap, multiplication, num1 + " * " + num2,minResult,maxResult);

        // Apply division
        if (num2 != 0 && num1 % num2 == 0) {
            int division1 = num1 / num2;
            addCalculation(calculationsMap, division1, num1 + " / " + num2,minResult,maxResult);
        }
        if (num1 != 0 && num2 % num1 == 0) {
            int division2 = num2 / num1;
            addCalculation(calculationsMap, division2, num2 + " / " + num1,minResult,maxResult);
        }
    }

    // Method to add a calculation to the map
    public static void addCalculation(Map<Integer, java.util.List<String>> calculationsMap, int result, String calculation,int minRange, int maxRange) {
        if (result >= minRange && result <= maxRange) {
            calculationsMap.putIfAbsent(result, new ArrayList<>());
            calculationsMap.get(result).add(calculation);
        }
    }

    // Method to print the map of calculations with sorted calculations within each set
    public static void printSortedCalculationsMap(Map<Integer, java.util.List<String>> calculationsMap) {
        for (Map.Entry<Integer, java.util.List<String>> entry : calculationsMap.entrySet()) {
            int result = entry.getKey();
            java.util.List<String> calculations = entry.getValue();
            Collections.sort(calculations, Comparator.comparingInt(OperationsCombinations::getFirstNumber));
            System.out.println("Result: " + result);
            System.out.println("Calculations:");
            for (String calculation : calculations) {
                System.out.println(calculation + " = ( " + result + " )");
            }
            System.out.println();
        }
    }

    // Helper method to get the first number in a calculation string
    public static int getFirstNumber(String calculation) {
        return Integer.parseInt(calculation.split(" ")[0]);
    }
}

	  
	  

}
