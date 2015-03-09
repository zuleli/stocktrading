package stock;
/* Author: Zule Li
 * Email:zule.li@hotmail.com
 * Last Modified Date:Mar.7,2015
 * */
import javax.swing.*;
import java.awt.*;

public class Graphic extends JDialog 
{
  Canvas canvas=new Canvas();
  String[] data0,data1,data2;
  
  public Graphic()
  {
    super();
    double[] tem=new double[]{3,80,6,3,30,8,3,23.5,45,3,24,35};
    canvas.data0=tem;
    JScrollPane scroll=new JScrollPane(canvas);

    scroll.setPreferredSize(new Dimension(400,300));
    Container content=getContentPane();
    content.add(scroll);
    pack();
    setVisible(true);
  }
  public void setData0(String[] ss0)
  {
    this.data0=ss0;
  }
   public void setData1(String[] ss1)
  {
    this.data1=ss1;
  }
   public void setData2(String[] ss2)
  {
    this.data2=ss2;
  }
 

  public static void main(String[] args)
  {
    Graphic graphic = new Graphic();
  }
}