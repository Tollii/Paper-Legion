package dragAndDrop;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class Piece extends Rectangle {
    GridPane a = new GridPane();
    private double oldPosX; // Nødvendig?
    private double oldPosY; // Nødvendig?
    private double hp;
    private Label healthbar;
    //private Rectangle piece;

    public Piece(double width, double height, double row, double column, double hp){
        super.setWidth(width);
        super.setHeight(height);
        super.setFill(Color.BLACK);
        setPosition(column*100,row*100);
        this.hp = hp;
        System.out.println(hp);
        String hpText = String.valueOf(hp);
        healthbar = new Label(hpText);
        healthbar.setTranslateX(column*100);
        healthbar.setTranslateY(row*100);
        healthbar.setTextFill(Color.WHITE);
        healthbar.setTextAlignment(TextAlignment.CENTER);


        a.getChildren().addAll(this, healthbar);



    }


    public void setPosition(double x, double y){
        super.setTranslateX(x);
        super.setTranslateY(y);
    }

    public Piece getObject(){
        return this;
    }

    public void takeDamage(){
        this.hp -=20;
        healthbar.setText(String.valueOf(hp));
    }

    public double getHp(){
        return hp;
    }



}
