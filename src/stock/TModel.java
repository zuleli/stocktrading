package stock;
/* Author: Zule Li
 * Email:zule.li@hotmail.com
 * Last Modified Date:Mar.7,2015
 * */

//Copyright (c) 2001 Home
import javax.swing.table.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TableModelListener; 
import javax.swing.event.TableModelEvent; 

/**
* @author Zule Li
*/
public class TModel extends AbstractTableModel{
private Vector data=new Vector();;
private int row;
private int column;
protected String[] columnNames;
private boolean[]editable;
private String id=" ";
public int MSU=1;






/**
* Constructor
*/

public TModel(String[] names,boolean[] edit,Vector data) {
   columnNames=names;
   editable=edit;
   this.data=data;
}

public TModel(String[] names,boolean[] edit) {
   columnNames=names;
   editable=edit;
 //  System.out.println("??"+"="+getColumnCount()+getColumnName(1));
}

public Vector getData() {
 return data;
}
public void setID(String id)
{
 this.id=id;
}
public String getID()
{
 return id;
}

public void setData(Vector newData) {
 data = newData;
 this.fireTableStructureChanged();
}
public void addData(String[] ss) {
 data.add(0,ss);
 this.fireTableDataChanged();
}
public void delete(int index)
{
   data.removeElementAt(index);
   this.fireTableDataChanged();
}
public void delete(String[] ss)
{
 
  for(int i=0;i<ss.length;i++)
  {
     delete(ss[i]);
  }
}
public void delete(String s)
{
 String tem="";
  for(int i=0;i<data.size();i++)
  {
     tem=((String[])(data.get(i)))[0];
     if(tem.equalsIgnoreCase(s))
     {
         data.removeElementAt(i);
         this.fireTableDataChanged();
         return;
     }
  }
}


public int getColumnCount() {
    
     return columnNames.length;
     
 }
 
 public int getRowCount() {
     return (data==null)?0:data.size();
 }

 public String getColumnName(int col) {
     return columnNames[col];
 }
 public String deleteCharacter(String s)
 {
   String result="0";
   char tem=' ';
   int length=s.length();
   for(int i=0;i<length;i++)
   {
     tem=s.charAt(i);
     if(tem=='.'&&i!=0)
     break;
     else if(tem=='.'&&i==0)
     {
       result="0";
       break;
     }
     
     if(Character.isDigit(tem))
     result=result+tem;
   }
   return ""+Long.parseLong(result);
 }
 public void setValueAt(Object value, int row, int col)
 {
//*     
     
     if(id.equalsIgnoreCase("catagoryModel"))
     {
       String[] ss=new String[2];
       if(col==0)
       {
         ss[0]=(String)value;
         if(check(ss[0],0))
         return;
         ss[1]=(String)getValueAt(row,1);
       }else if(col==1)
       {
         ss[1]=deleteCharacter((String)value);
         //if(Character.isDigit()
         ss[0]=(String)getValueAt(row,0);
       }
      
     }
     
	       fireTableCellUpdated(row, col);
      //********/
 }

 public Object getValueAt(int row, int col)
 {
// System.out.println(row+","+col);
   return ((String[])(data.get(row)))[col];


  }

 public Class getColumnClass(int c) {
     return (new String("aa")).getClass();
 }

 public boolean isCellEditable(int row, int col)
 { return false; }
 public void setNames(String[] names)
 {
   this.columnNames=names;
   editable=new boolean[names.length];
 }
 public void update(int index)
 {
   this.fireTableRowsUpdated(index,index);
 }

 public void updateItem(String id,String operator,String type,String accountno)
 {
   int index=getIndex(id,0);
   String[] ss=(String[])(data.get(index));
   if(type.equalsIgnoreCase("IN"))
   {
     ss[5]="IN";
     ss[6]="NA";
     if(this.getColumnCount()>11)
     ss[10]=operator;
   }else if(type.equalsIgnoreCase("OUT"))
   {
     ss[5]="OUT";
     ss[6]=accountno;
     if(this.getColumnCount()>11)
     ss[10]=operator;
   }
   
   this.fireTableRowsUpdated(index,index);
 }
private int getIndex(String value,int column)
{
 String tem="";
 for(int i=0;i<data.size();i++)
   {
       tem=((String[])(data.get(i)))[column];
       if(tem.equalsIgnoreCase(value))
       return i;
   }
   return 1000000000;
}

public boolean isUnConfirmed(int row)
{
String[] ss=(String[])(data.get(row));
if(ss.length==5&&ss[4].equalsIgnoreCase("UC"))
return true;
else
return false;
}
public String cancelReturnMark(int row)
{
String[] ss=(String[])(data.get(row));
if(ss[2].equalsIgnoreCase("Ready"))
{
 ss[2]="Return Canceled";
 this.fireTableCellUpdated(row,2);
 return ss[0];
}
return "NA";
}

public void setConfirmedIDs(String[] ids,boolean[] result)
 {
   int index=0;
   for(int i=0;i<ids.length;i++)
   {
       index=getIndex(ids[i],0);
       if(result[i])
       {
         ((String[])(data.get(index)))[4]="CMED";
         ((String[])(data.get(index)))[2]=(new java.sql.Date(System.currentTimeMillis())).toString();
         this.fireTableCellUpdated(index,2);
       }
   }

 
 }
 
 public String[] getUnConfirmedIDs()
 {
   Vector tem=new Vector();
   String[] ss=null;
   for(int i=0;i<data.size();i++)
   {
       ss=(String[])(data.get(i));
       if(ss.length==5&&ss[4].equalsIgnoreCase("UC"))
       tem.add(ss[0]);
   }
   ss=new String[tem.size()];

   for(int i=0;i<tem.size();i++)
   {
       ss[i]=(String)(tem.get(i));
   }
   
   return ss;
 }
  public void markReturn(String id)
 {
   String[] ss=null;
   for(int i=0;i<data.size();i++)
   {
       ss=(String[])(data.get(i));
       if(ss[0].equalsIgnoreCase(id))
       {
       ss[2]="Ready";
       this.fireTableCellUpdated(i,2);
       return;
       }
   }
  this.fireTableDataChanged();
 }
 public boolean check(String value,int column)
 {
   boolean result=false;
   String tem="";
   for(int i=0;i<data.size();i++)
   {
     tem=((String[])(data.get(i)))[column];
     if(value.equalsIgnoreCase(tem))
     {         
       return true;
     }
   }
   return result;
 }
 public void update(String[] ss)
 {
   String[] dt=null;
   for(int i=0;i<data.size();i++)
   {
     dt=(String[])(data.get(i));
     if(dt[0].equalsIgnoreCase(ss[0]))
     {
         if(id.equalsIgnoreCase("CustomerModel"))
         {
               dt[1]=ss[1];
               dt[2]=ss[2];
               dt[3]=ss[3];
               this.fireTableDataChanged();
               return;
         }
          if(id.equalsIgnoreCase("itemModel"))
         {
               dt[1]=ss[1];
               dt[2]=ss[2];
               dt[3]=ss[3];
               dt[4]=ss[4];
              // dt[5]=ss[5];
               this.fireTableDataChanged();
               return;
         }

     }

   }

 }

}

