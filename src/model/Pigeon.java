package model;

import java.awt.Point;
import java.util.List;

import controller.Square;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pigeon extends Circle implements Runnable {

	private boolean isAlive;
	private Square square;
	private String name;
	private boolean affraid;
	private int randomX, randomY;

	public Pigeon(int x, int y, Color color, Square square, String name) {
		super(x, y, 10, color);
		this.isAlive = true;
		this.square = square;
		this.name = name;
		this.affraid = false;
	}

	@Override
	public void run() {
		while (isAlive) {
			live();
		}
	}

	private void live() {
		if (!affraid)
			goToFood();
		else {

			move(randomX, randomY);
		}

		try {
			if (affraid)
				Thread.sleep(8);
			else
				Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void goToFood() {
		List<Food> foods = square.getFoodList();
		Food f = findFood(foods);

		if (f != null) {
			System.out.println(this.name + "is moving");
			moveTo(f);
		}
	}

	private Food findFood(List<Food> foods) {
		Food f = null;

		if (foods.size() > 0) {
			Double distance = Double.MAX_VALUE;

			for (int i = 0; i < foods.size(); i++) {
				if (calculateDistance(foods.get(i).getPoint()) < distance) {
					distance = calculateDistance(foods.get(i).getPoint());
					f = foods.get(i);
				}
			}
		}

		return f;
	}

	private Double calculateDistance(Point p1) {
		Double sumX = Math.pow(p1.x - this.getCenterX(), 2);
		Double sumY = Math.pow(p1.y - this.getCenterY(), 2);

		return Math.sqrt(sumX + sumY);
	}

	private void moveTo(Food f) {
		Platform.runLater(() -> {
			if (this.getCenterX() == f.getPoint().x && this.getCenterY() == f.getPoint().y) {
				if (f.isFresh()) {
					System.out.println("The " + this.name + " pigeon is eating!");
					this.square.removeFood(f);
				} else {
					// The food is rotten
					this.square.removeRottenFood(f);
					System.out.println("The pigeon doesn't like this food");
				}
			} else {
				if (this.getCenterX() < f.getPoint().x)
					this.setCenterX(this.getCenterX() + 1);

				if (this.getCenterX() > f.getPoint().x)
					this.setCenterX(this.getCenterX() - 1);

				if (this.getCenterY() < f.getPoint().y)
					this.setCenterY(this.getCenterY() + 1);

				if (this.getCenterY() > f.getPoint().y)
					this.setCenterY(this.getCenterY() - 1);

			}
		});
	}

	private void move(double x, double y) {
		// Move to the coordinates
		Platform.runLater(() -> {
			if (this.getCenterX() == x && this.getCenterY() == y) {
				this.affraid = false;
			} else {
				if (this.getCenterX() < x)
					this.setCenterX(this.getCenterX() + 1);

				if (this.getCenterX() > x)
					this.setCenterX(this.getCenterX() - 1);

				if (this.getCenterY() < y)
					this.setCenterY(this.getCenterY() + 1);

				if (this.getCenterY() > y)
					this.setCenterY(this.getCenterY() - 1);
			}
		});
	}

	public void setAffraid(int randomX, int randomY) {
		this.affraid = true;

		this.randomX = randomX;
		this.randomY = randomY;

	}

}
