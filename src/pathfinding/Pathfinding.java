package pathfinding;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import finished.List;

public class Pathfinding {

	public static void main(String[] args) {
		new Pathfinding();
	}

	private JFrame frame;
	private PathfindingEnvironment panel;
	private int numbersWidthCount = 19, numbersHeightCount = 37, numbersWidth = 40, numbersHeight = 20;
	private int eAddLeft = 0, eAddRight = 0, eAddUp = 1, eAddDown = 0;
	private int eSightLeft = 5, eSightRight = 5, eSightUp = 5, eSightDown = 5;

	public Pathfinding() {
		frame = new JFrame("Pathfinding");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 800);

		panel = new PathfindingEnvironment(0, 0, 800, 800);
		frame.setLayout(null);
		frame.add(panel);

		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	private class PathfindingEnvironment extends JPanel {

		private JLabel[][] numbers;

		private JPanel enemy;

		private List<Point> pathPoints;

		private int pIdX, pIdY, eIdX, eIdY;

		public PathfindingEnvironment(int x, int y, int width, int height) {
			super();
			setBounds(x, y, width, height);
			setLayout(null);

			pathPoints = new List<>();
			createGrid();
			pIdX = -1;
			pIdY = -1;
			eIdX = -1;
			eIdY = -1;

			addMouseMotionListener(new MouseMotionAdapter() {

				@Override
				public void mouseMoved(MouseEvent e) {
					if (e.getX() > 10 && e.getX() < 770 && e.getY() > 10 && e.getY() < 750) {
						if (pIdX != -1 && pIdY != -1) {
							if (!numbers[pIdX][pIdY].getText().equals("##")
									&& !numbers[pIdX][pIdY].getText().equals("EE")) {
								numbers[pIdX][pIdY].setText("-1");
							}
						}
						if (!numbers[(e.getX() - 10) / numbersWidth][(e.getY() - 10) / numbersHeight].getText()
								.equals("##")
								&& !numbers[(e.getX() - 10) / numbersWidth][(e.getY() - 10) / numbersHeight].getText()
										.equals("EE")) {
							numbers[(e.getX() - 10) / numbersWidth][(e.getY() - 10) / numbersHeight].setText("00");
						}
						pIdX = (e.getX() - 10) / numbersWidth;
						pIdY = (e.getY() - 10) / numbersHeight;
					}
				}

			});
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						fieldScoreMeasure();
					}
					if (e.getButton() == MouseEvent.BUTTON2) {
						setEnemy(e.getX(), e.getY());
					}
					if (e.getButton() == MouseEvent.BUTTON3) {
						markField();
					}
				}
			});
		}

		private void setEnemy(int x, int y) {
			if (eCollidesWithNextTile((x - 10) / numbersWidth, (y - 10) / numbersHeight)) {
				return;
			}
			if (eIdX != -1 && eIdY != -1) {
				numbers[eIdX][eIdY].setForeground(Color.BLACK);
			}
			if (!numbers[(x - 10) / numbersWidth][(y - 10) / numbersHeight].getText().equals("##")) {
				numbers[(x - 10) / numbersWidth][(y - 10) / numbersHeight].setForeground(Color.BLUE);
				enemy.setLocation(10 + numbersWidth * ((x - 10) / numbersWidth) - numbersWidth * eAddLeft,
						10 + numbersHeight * ((y - 10) / numbersHeight) - numbersHeight * eAddUp);
			}
			eIdX = (x - 10) / numbersWidth;
			eIdY = (y - 10) / numbersHeight;
		}

		private void markField() {
			if (numbers[pIdX][pIdY].getText().equals("00")) {
				numbers[pIdX][pIdY].setText("##");
				return;
			}
			if (numbers[pIdX][pIdY].getText().equals("##")) {
				numbers[pIdX][pIdY].setText("00");
				return;
			}
		}

		private boolean checkCoordsInSight(int idX, int idY) {
			if (eIdX == -1 || eIdY == -1) {
				return false;
			}
			if (eIdX - eSightLeft <= idX && eIdX + eSightRight >= idX && eIdY - eSightUp <= idY
					&& eIdY + eSightDown >= idY) {
				return true;
			}
			return false;
		}

		private void fieldScoreMeasure() {
			reset();

			if (!checkCoordsInSight(pIdX, pIdY) || numbers[pIdX][pIdY].getText().equals("##")
					|| eCollidesWithNextTile(pIdX, pIdY)) {
				pathPoints.clear();
				return;
			}

			// pIdX,pIdY,priorFieldScore
			List<String> list = new List<>();

			if (pIdX - 1 > -1 && checkCoordsInSight(pIdX - 1, pIdY)) {
				if (!numbers[pIdX - 1][pIdY].getText().equals("##")) {
					list.add((pIdX - 1) + "," + pIdY + "," + 0);
				}
			}
			if (pIdX + 1 < numbersWidthCount && checkCoordsInSight(pIdX + 1, pIdY)) {
				if (!numbers[pIdX + 1][pIdY].getText().equals("##")) {
					list.add((pIdX + 1) + "," + pIdY + "," + 0);
				}
			}
			if (pIdY - 1 > -1 && checkCoordsInSight(pIdX, pIdY - 1)) {
				if (!numbers[pIdX][pIdY - 1].getText().equals("##")) {
					list.add(pIdX + "," + (pIdY - 1) + "," + 0);
				}
			}
			if (pIdY + 1 < numbersHeightCount && checkCoordsInSight(pIdX, pIdY + 1)) {
				if (!numbers[pIdX][pIdY + 1].getText().equals("##")) {
					list.add(pIdX + "," + (pIdY + 1) + "," + 0);
				}
			}
			int counter = 0;
			long time = System.currentTimeMillis();
			while (list.length() > 0) {
				numbers[Integer.parseInt(list.get(0).split(",")[0])][Integer.parseInt(list.get(0).split(",")[1])]
						.setText((Integer.parseInt(list.get(0).split(",")[2]) + 1) < 10
								? ("0" + (Integer.parseInt(list.get(0).split(",")[2]) + 1))
								: "" + (Integer.parseInt(list.get(0).split(",")[2]) + 1));

				if (Integer.parseInt(list.get(0).split(",")[0]) - 1 > -1
						&& !(Integer.parseInt(list.get(0).split(",")[0]) - 1 == pIdX
								&& Integer.parseInt(list.get(0).split(",")[1]) == pIdY)
						&& checkCoordsInSight(Integer.parseInt(list.get(0).split(",")[0]) - 1,
								Integer.parseInt(list.get(0).split(",")[1]))) {
					if (numbers[Integer.parseInt(list.get(0).split(",")[0]) - 1][Integer
							.parseInt(list.get(0).split(",")[1])].getText().equals("-1")) {
						if (!list.contains(
								(Integer.parseInt(list.get(0).split(",")[0]) - 1) + "," + list.get(0).split(",")[1]
										+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1))) {
							list.add((Integer.parseInt(list.get(0).split(",")[0]) - 1) + "," + list.get(0).split(",")[1]
									+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1));
						}
					}
				}
				if (Integer.parseInt(list.get(0).split(",")[0]) + 1 < numbersWidthCount
						&& !(Integer.parseInt(list.get(0).split(",")[0]) + 1 == pIdX
								&& Integer.parseInt(list.get(0).split(",")[1]) == pIdY)
						&& checkCoordsInSight(Integer.parseInt(list.get(0).split(",")[0]) + 1,
								Integer.parseInt(list.get(0).split(",")[1]))) {
					if (numbers[Integer.parseInt(list.get(0).split(",")[0]) + 1][Integer
							.parseInt(list.get(0).split(",")[1])].getText().equals("-1")) {
						if (!list.contains(
								(Integer.parseInt(list.get(0).split(",")[0]) + 1) + "," + list.get(0).split(",")[1]
										+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1))) {
							list.add((Integer.parseInt(list.get(0).split(",")[0]) + 1) + "," + list.get(0).split(",")[1]
									+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1));
						}
					}
				}
				if (Integer.parseInt(list.get(0).split(",")[1]) - 1 > -1
						&& !(Integer.parseInt(list.get(0).split(",")[0]) == pIdX
								&& Integer.parseInt(list.get(0).split(",")[1]) - 1 == pIdY)
						&& checkCoordsInSight(Integer.parseInt(list.get(0).split(",")[0]),
								Integer.parseInt(list.get(0).split(",")[1]) - 1)) {
					if (numbers[Integer.parseInt(list.get(0).split(",")[0])][Integer.parseInt(list.get(0).split(",")[1])
							- 1].getText().equals("-1")) {
						if (!list.contains(
								list.get(0).split(",")[0] + "," + (Integer.parseInt(list.get(0).split(",")[1]) - 1)
										+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1))) {
							list.add(list.get(0).split(",")[0] + "," + (Integer.parseInt(list.get(0).split(",")[1]) - 1)
									+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1));
						}
					}
				}
				if (Integer.parseInt(list.get(0).split(",")[1]) + 1 < numbersHeightCount
						&& !(Integer.parseInt(list.get(0).split(",")[0]) == pIdX
								&& Integer.parseInt(list.get(0).split(",")[1]) + 1 == pIdY)
						&& checkCoordsInSight(Integer.parseInt(list.get(0).split(",")[0]),
								Integer.parseInt(list.get(0).split(",")[1]) + 1)) {
					if (numbers[Integer.parseInt(list.get(0).split(",")[0])][Integer.parseInt(list.get(0).split(",")[1])
							+ 1].getText().equals("-1")) {
						if (!list.contains(
								list.get(0).split(",")[0] + "," + (Integer.parseInt(list.get(0).split(",")[1]) + 1)
										+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1))) {
							list.add(list.get(0).split(",")[0] + "," + (Integer.parseInt(list.get(0).split(",")[1]) + 1)
									+ "," + (Integer.parseInt(list.get(0).split(",")[2]) + 1));
						}
					}
				}

				list.remove(0);
				counter++;
			}

			if (eIdX == -1 || eIdY == -1) {
				return;
			}

			Point minNumberCoords = new Point(eIdX, eIdY), tmpCoords;
			int minNumber = Integer.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText());
			boolean[] unavailableFields = new boolean[8];
			boolean noOptionsLeft = false;
			pathPoints.clear();
			while (minNumber > 0 && !noOptionsLeft) {
				for (int i = 0; i < unavailableFields.length; i++) {
					unavailableFields[i] = true;
				}
				tmpCoords = new Point(minNumberCoords);
				if (tmpCoords.x - 1 > -1 && tmpCoords.y - 1 > -1) {
					if (!numbers[tmpCoords.x - 1][tmpCoords.y - 1].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x - 1, tmpCoords.y - 1)
								&& Integer.parseInt(numbers[tmpCoords.x - 1][tmpCoords.y - 1].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x - 1][tmpCoords.y - 1].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x - 1, tmpCoords.y - 1);
							unavailableFields[0] = false;
						}
					}
				}
				if (tmpCoords.y - 1 > -1) {
					if (!numbers[tmpCoords.x][tmpCoords.y - 1].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x, tmpCoords.y - 1)
								&& Integer.parseInt(numbers[tmpCoords.x][tmpCoords.y - 1].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x][tmpCoords.y - 1].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x, tmpCoords.y - 1);
							unavailableFields[1] = false;
						}
					}
				}
				if (tmpCoords.x + 1 < numbersWidthCount && tmpCoords.y - 1 > -1) {
					if (!numbers[tmpCoords.x + 1][tmpCoords.y - 1].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x + 1, tmpCoords.y - 1)
								&& Integer.parseInt(numbers[tmpCoords.x + 1][tmpCoords.y - 1].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x + 1][tmpCoords.y - 1].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x + 1, tmpCoords.y - 1);
							unavailableFields[2] = false;
						}
					}
				}
				if (tmpCoords.x - 1 > -1) {
					if (!numbers[tmpCoords.x - 1][tmpCoords.y].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x - 1, tmpCoords.y)
								&& Integer.parseInt(numbers[tmpCoords.x - 1][tmpCoords.y].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x - 1][tmpCoords.y].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x - 1, tmpCoords.y);
							unavailableFields[3] = false;
						}
					}
				}
				if (tmpCoords.x + 1 < numbersWidthCount) {
					if (!numbers[tmpCoords.x + 1][tmpCoords.y].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x + 1, tmpCoords.y)
								&& Integer.parseInt(numbers[tmpCoords.x + 1][tmpCoords.y].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x + 1][tmpCoords.y].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x + 1, tmpCoords.y);
							unavailableFields[4] = false;
						}
					}
				}
				if (tmpCoords.x - 1 > -1 && tmpCoords.y + 1 < numbersHeightCount) {
					if (!numbers[tmpCoords.x - 1][tmpCoords.y + 1].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x - 1, tmpCoords.y + 1)
								&& Integer.parseInt(numbers[tmpCoords.x - 1][tmpCoords.y + 1].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x - 1][tmpCoords.y + 1].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x - 1, tmpCoords.y + 1);
							unavailableFields[5] = false;
						}
					}
				}
				if (tmpCoords.y + 1 < numbersHeightCount) {
					if (!numbers[tmpCoords.x][tmpCoords.y + 1].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x, tmpCoords.y + 1)
								&& Integer.parseInt(numbers[tmpCoords.x][tmpCoords.y + 1].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x][tmpCoords.y + 1].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x, tmpCoords.y + 1);
							unavailableFields[6] = false;
						}
					}
				}
				if (tmpCoords.x  + 1 < numbersWidthCount && tmpCoords.y + 1 < numbersWidthCount) {
					if (!numbers[tmpCoords.x + 1][tmpCoords.y + 1].getText().equals("##")) {
						if (!eCollidesWithNextTile(tmpCoords.x + 1, tmpCoords.y + 1)
								&& Integer.parseInt(numbers[tmpCoords.x + 1][tmpCoords.y + 1].getText()) <= Integer
										.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText())
								&& !numbers[tmpCoords.x + 1][tmpCoords.y + 1].getText().equals("-1")) {
							minNumberCoords.setLocation(tmpCoords.x + 1, tmpCoords.y + 1);
							unavailableFields[7] = false;
						}
					}
				}
				
				for (int i = 0; i < unavailableFields.length; i++) {
					if (!unavailableFields[i]) {
						minNumber = Integer.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText());
						System.out
								.print(Integer.parseInt(numbers[minNumberCoords.x][minNumberCoords.y].getText()) + " ");
						numbers[minNumberCoords.x][minNumberCoords.y].setForeground(Color.RED);
						pathPoints.add(new Point(minNumberCoords));
						break;
					}
					if (unavailableFields[i] && i == unavailableFields.length - 1) {
						noOptionsLeft = true;
						resetColors();
						pathPoints.clear();
					}
				}
			}
			System.out.println();
			System.out.println(counter + "; used time: " + (System.currentTimeMillis() - time));
		}

		private boolean eCollidesWithNextTile(int idX, int idY) {
			boolean eCollides = false;
			for (int i = 1; i <= eAddUp && !eCollides; i++) {
				if (numbers[idX][idY - i].getText().equals("##")) {
					eCollides = true;
				}
			}
			for (int i = 1; i <= eAddDown && !eCollides; i++) {
				if (numbers[idX][idY + i].getText().equals("##")) {
					eCollides = true;
				}
			}
			for (int i = 1; i <= eAddLeft && !eCollides; i++) {
				if (numbers[idX - i][idY].getText().equals("##")) {
					eCollides = true;
				}
			}
			for (int i = 1; i <= eAddRight && !eCollides; i++) {
				if (numbers[idX + i][idY].getText().equals("##")) {
					eCollides = true;
				}
			}
			return eCollides;
		}

		private void reset() {
			for (int i = 0; i < numbers.length; i++) {
				for (int j = 0; j < numbers[i].length; j++) {
					if (!numbers[i][j].getForeground().equals(Color.BLUE)) {
						numbers[i][j].setForeground(Color.BLACK);
					}
					if (numbers[i][j].getText().equals("##") || numbers[i][j].getText().equals("00")
							|| numbers[i][j].getText().equals("EE")) {
						continue;
					}
					numbers[i][j].setText("-1");
				}
			}
		}

		private void resetColors() {
			for (int i = 0; i < numbers.length; i++) {
				for (int j = 0; j < numbers[i].length; j++) {
					if (!numbers[i][j].getForeground().equals(Color.BLUE)) {
						numbers[i][j].setForeground(Color.BLACK);
					}
				}
			}
		}

		private void createGrid() {
			numbers = new JLabel[numbersWidthCount][numbersHeightCount];
			for (int i = 0; i < numbers.length; i++) {
				for (int j = 0; j < numbers[i].length; j++) {
					numbers[i][j] = new JLabel("-1");
					numbers[i][j].setFont(new Font(Font.MONOSPACED, Font.PLAIN, numbersHeight));
					numbers[i][j].setBounds(10 + i * numbersWidth, 10 + j * numbersHeight, numbersWidth, numbersHeight);
					numbers[i][j].setBackground(Color.LIGHT_GRAY);
					add(numbers[i][j], 0);
				}
			}
			enemy = new JPanel();
			enemy.setBounds(-numbersWidth * (1 + eAddLeft + eAddRight), -numbersHeight * (1 + eAddUp + eAddDown),
					numbersWidth * (1 + eAddLeft + eAddRight), numbersHeight * (1 + eAddUp + eAddDown));
			enemy.setBackground(Color.GREEN);
			add(enemy);
			new MovementController().start();
		}

		private class MovementController extends Thread {

			@Override
			public void run() {
				long counter = 0;
				while (counter < Long.MAX_VALUE) {
					if (counter % 10 == 0 && pathPoints.length() > 0) {
						enemy.setLocation(10 + numbersWidth * pathPoints.get(0).x - numbersWidth * eAddLeft,
								10 + numbersHeight * pathPoints.get(0).y - numbersHeight * eAddUp);
						numbers[eIdX][eIdY].setForeground(Color.BLACK);
						eIdX = pathPoints.get(0).x;
						eIdY = pathPoints.get(0).y;
						numbers[eIdX][eIdY].setForeground(Color.BLUE);
						pathPoints.remove(0);
					}
					counter++;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

}
