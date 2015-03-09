package stock;

/* Author: Zule Li
 * Email:zule.li@hotmail.com
 * Last Modified Date:Mar.7,2015
 * */

import java.sql.Date;
import java.util.Collections;
import java.util.Vector;
import javax.swing.*;
//import oracle.jdeveloper.layout.XYLayout;
//import oracle.jdeveloper.layout.XYConstraints;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class Main extends JFrame implements ActionListener, Runnable {
	private TableModel tableModel1 = new javax.swing.table.DefaultTableModel();
	private JPanel customerPane = new JPanel();
	private JScrollPane jScrollPane6_cus = new JScrollPane();
	private JTable table = new JTable();
	// private XYLayout xYLayout1 = new XYLayout();
	private JPanel control_cus = new JPanel();
	private JComboBox date = new JComboBox();
	private JLabel jLabel1_cus = new JLabel();
	private JButton detail = new JButton();
	private JButton jiajian = new JButton();
	private JComboBox ids = new JComboBox();
	private JLabel jLabel1_cus1 = new JLabel();
	public JLabel display = new JLabel();
	private JTextField dadan = new JTextField();
	// private XYLayout xYLayout2 = new XYLayout();
	// private XYLayout xYLayout3 = new XYLayout();
	private JButton shous = new JButton();
	private JButton selection = new JButton();
	private Vector data = new Vector();

	private TModel model = null;
	private JButton fdetail = new JButton();
	private JButton addition = new JButton();
	private String runID = "";
	private boolean stop = false;
	private Database db = null;
	private JButton step2 = new JButton();

	public Main() {

		try {
			init();
			jbInit();
			enableEvents(AWTEvent.WINDOW_EVENT_MASK);
			date.setSelectedIndex(4);
			ids.setSelectedItem("600688");
			// detailOp();
			pack();
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void init() {
		this.actionListener(jiajian);
		this.actionListener(detail);
		this.actionListener(shous);
		this.actionListener(selection);
		this.actionListener(fdetail);
		this.actionListener(addition);
		this.actionListener(step2);
		db = new Database(this);
		Vector tem = db.getStockIDS();
		// List list=tem.subList(0,tem.size()-1);
		Vector tem2 = new Vector();
		for (int i = 0; i < tem.size(); i++) {
			tem2.add(((String[]) (tem.get(i)))[0]);
		}
		Collections.sort(tem2);
		String ss = null;
		for (int i = 0; i < tem2.size(); i++) {
			ss = (String) (tem2.get(i));
			ids.addItem(ss);
		}
		addDate(date, 30, true);// true means backward

		String[] columnNames = new String[] { "Time", "Price", "Shou",
				"Amount", "Type" };
		boolean[] edit = new boolean[] { false, false, false, false };
		model = new TModel(columnNames, edit);
		model.setID("model");
		TableSorter sorter = new TableSorter(model); // ADDED THIS
		// JTable table = new JTable(new MyTableModel()); //OLD
		table = new JTable(sorter); // NEW
		sorter.setTableHeader(table.getTableHeader()); // ADDED THIS
	}

	private void addDate(JComboBox box, int days, boolean back) {
		long time = System.currentTimeMillis();
		java.sql.Date date = null;
		for (long i = 0; i < days; i++) {
			if (back)
				date = new java.sql.Date(time - i * 1000 * 24 * 60 * 60);
			else
				date = new java.sql.Date(time + i * 1000 * 24 * 60 * 60);
			box.addItem(date);
		}
	}

	private Vector getPrices() {
		Vector prices = new Vector();
		String[] ss = null;
		float m = 0;
		int index = 0;
		boolean status = true;
		for (int i = 0; i < data.size(); i++) {
			ss = (String[]) (data.get(i));
			for (int j = 0; j < 6; j++) {
				m = Float.parseFloat(ss[11 + j]);
				status = true;
				for (int n = 0; n < prices.size(); n++) {
					if (m == ((Float) prices.get(n)).floatValue()) {
						status = false;
						break;
					}
				}
				if (status) {
					prices.add(Float.valueOf("" + m));
				}
			}

		}
		Collections.sort(prices);
		return prices;
	}

	private Vector getGuaData(Vector prices) {
		String[] ss = null;
		String[] nss = null;
		Vector ndata = new Vector();
		float m = 0;
		int index = 0;
		for (int i = 0; i < data.size(); i++) {
			ss = (String[]) (data.get(i));
			nss = new String[prices.size() + 1 + 4];
			for (int j = 0; j < 6; j++) {
				m = Float.parseFloat(ss[11 + j]);
				for (int n = 0; n < prices.size(); n++) {
					if (m == ((Float) prices.get(n)).floatValue()) {
						index = n;
						break;
					}
				}
				nss[index + 4] = ss[5 + j];
				if (j == 3)
					nss[prices.size() + 4] = "" + (index + 4);// this is to
																// indicate
																// where "Sell"
																// starts
			}
			nss[0] = ss[0];
			nss[1] = ss[1];
			nss[2] = ss[2];
			nss[3] = ss[4];
			ndata.add(nss);
		}
		return ndata;
	}

	private void fdetailOp() {
		String[] names = new String[] { "Time", "Price", "Shou", "Money",
				"Type", "A1", "A2", "A3", "V1", "V2", "V3", "AP1", "AP2",
				"A3P", "VP1", "VP2", "VP3", "Nei", "Wai" };
		model.setNames(names);
		model.setID("FDetail");
		model.setData(data);

	}

	private void jiajianOp() {
		if (!stop)
			stop = true;
		if (runID.equalsIgnoreCase("NA")) {
			stop = false;
			display.setText("Starting");
		}
		runID = "CAL";
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);

			} catch (InterruptedException ie) {
			}
			if (runID.equalsIgnoreCase("CAL"))
				Cal();
			if (runID.equalsIgnoreCase("CAL0"))
				Cal0();
			runID = "NA";
		}

	}

	private void Cal() {

		java.sql.Date now = (java.sql.Date) (date.getSelectedItem());
		String code = "";
		Vector adata = new Vector();
		Vector udata = new Vector();
		Vector acode = new Vector();
		Vector ucode = new Vector();

		String[] tem = null;
		String s = dadan.getText().trim();
		s = (s == null || s.length() == 0) ? "0" : s;
		int days = Integer.parseInt(s);
		if (days == 0)
			return;
		Vector compdatash = db.getRawdata("1A0001", now, days);
		Vector compdatasz = db.getRawdata("399001", now, days);
		int size = ids.getItemCount();
		int count = 0;
		for (int i = 0; i < size; i++) {
			count++;
			code = (String) (ids.getItemAt(i));
			// code="600688";
			if ((db.getShiChang(code)).equalsIgnoreCase("SH"))
				tem = this.tongJi(code, now, days, compdatash);
			else
				tem = this.tongJi(code, now, days, compdatasz);
			if (tem != null) {
				display.setText(count + "/" + size + " :" + code + "  P");
				if (db.check(code)) {
					ucode.add(code);
					udata.add(tem);
				} else {
					acode.add(code);
					adata.add(tem);
				}
			}
			if (stop) {
				display.setText("Stopped");
				return;
			}
		}

		if (adata != null)
			db.addCalculation(acode, now, adata, days);
		if (udata != null)
			db.updateCalculation(ucode, now, udata, days);

		/*
		 * Vector prices=getPrices(); Vector ndata=getGuaData(prices); String[]
		 * names=new String[prices.size()+4]; names[0]="Time"; names[1]="Price";
		 * names[2]="Shou"; names[3]="Type"; for(int i=0;i<prices.size();i++) {
		 * names[i+4]=""+((Float)(prices.get(i))).floatValue(); }
		 * model.setNames(names); model.setID("JiaJian"); model.setData(ndata);
		 * //
		 */

	}

	private int getIndex(Vector prices, String value) {
		int index = 0;
		float f1 = Float.parseFloat(value);
		for (int i = 0; i < prices.size(); i++) {
			if (f1 == ((Float) (prices.get(i))).floatValue()) {
				index = i;
				return i;
			}
		}
		return index;
	}

	public String[] tongJi(String code, java.sql.Date ndate, int days,
			Vector compdata) {

		// float circleGu=db.getLiaoTong(code)*10000;
		Vector rawdata;
		Double[] ss0 = null, css0 = null;
		Double[] ss_1 = null, css_1;
		// int ii;
		// if((ii=code.indexOf("A"))!=-1)
		// else if((ii=code.indexOf("B"))!=-1)
		// compdata=db.getRawdata("399001",ndate,days);
		// else
		// return null;

		if (compdata == null || compdata.size() == 0)
			return null;
		rawdata = db.getRawdata(code, ndate, days);
		if (rawdata == null || rawdata.size() == 0)
			return null;
		double[] prices = new double[compdata.size()];
		double[] pricesDif = new double[compdata.size()];
		double[] priceW = new double[compdata.size()];
		double[] priceDW = new double[compdata.size()];
		double[] VirP = new double[compdata.size()];
		double[] amounts = new double[compdata.size()];
		double[] amountsD = new double[compdata.size()];
		double[] amountsDif = new double[compdata.size()];
		double[] dif = new double[compdata.size()];
		double[] difD = new double[compdata.size()];
		double[] difDS = new double[compdata.size()];
		double[] difDr = new double[compdata.size()];
		double[] difDSr = new double[compdata.size()];
		double[] fan = new double[compdata.size()];
		double[] fanD = new double[compdata.size()];
		double[] shoubi = new double[compdata.size()];
		double[] shoubiD = new double[compdata.size()];
		double[] caibuyR = new double[compdata.size()];
		double[] caisellR = new double[compdata.size()];

		// p.p(rawdata.size()+" size");
		double[] base = null;// new double[((Double[])rawdata.get(0)).length];
		double[] tem = new double[16];
		double[] ctem = new double[3];
		double[] cbase = new double[3];

		double price = 0, amount = 0, difv = 0, difDv = 0, difDSv = 0, fanv = 0, totalAmount = 0;
		long totalBi = 0;
		for (int i = 0; i < rawdata.size(); i++) {
			ss0 = (Double[]) rawdata.get(i);
			css0 = (Double[]) compdata.get(i);
			if (i == 0) {
				base = this.getBaseV(rawdata);
				cbase[0] = (css0[1] == null) ? 123456789 : css0[0]
						.doubleValue();
				cbase[1] = (css0[1] == null) ? 123456789 : css0[1]
						.doubleValue();
				cbase[2] = (css0[2] == null) ? 123456789 : css0[2]
						.doubleValue();
			}

			tem[0] = (ss0[0] == null) ? base[0] : ss0[1].doubleValue();
			tem[1] = (ss0[1] == null) ? base[1] : ss0[1].doubleValue();
			tem[2] = (ss0[2] == null) ? base[2] : ss0[2].doubleValue();
			ctem[0] = (css0[0] == null) ? cbase[0] : css0[0].doubleValue();
			ctem[1] = (css0[1] == null) ? cbase[1] : css0[1].doubleValue();
			ctem[2] = (css0[2] == null) ? cbase[2] : css0[2].doubleValue();

			shoubi[i] = tem[2];

			// totalAmount=totalAmount+tem[0]*tem[2];
			prices[i] = tem[1] / tem[2] / 100;
			pricesDif[i] = (tem[1] / tem[2] - base[1] / base[2])
					/ (base[1] / base[2])
					- (ctem[1] / ctem[2] - cbase[1] / cbase[2])
					/ (cbase[1] / cbase[2]);
			if (i == 0)
				pricesDif[i] = 0;
			if (Double.isNaN(pricesDif[i]))
				pricesDif[i] = 0;
			price = price + pricesDif[i];// indicate it perform better than da
											// pang

			amounts[i] = tem[2] * tem[0];
			amountsDif[i] = ((tem[2] * tem[0] - base[2] * base[0]) / (base[2] * base[0]))
					- ((ctem[2] * ctem[0] - cbase[2] * cbase[0]) / (cbase[2] * cbase[0]));
			if (i == 0)
				amountsDif[i] = 0;// if(amounts[i]>100||amounts[i]<100)
			// amounts[i]=0;
			// p.p(""+tem[2]*tem[0]);
			// p.p(""+(base[2]*base[0]));
			if (Double.isNaN(amountsDif[i]))
				amountsDif[i] = 0;
			amount = amount + amountsDif[i];
			// p.p("i="+i+"  >>> "+amounts[i]+"-----"+tem[2]+"=?"+base[2]+"  "+
			// tem[0]+"=?"+base[0]+"  "+ctem[2]+"=?"+cbase[2]+"  "+
			// ctem[0]+"=?"+cbase[0]);
			base[0] = tem[0];
			base[1] = tem[1];
			base[2] = tem[2];
			cbase[1] = ctem[1];
			cbase[2] = ctem[2];

			// ************Dif**************/

			tem[3] = (ss0[3] == null) ? base[3] : ss0[3].doubleValue();
			tem[4] = (ss0[4] == null) ? base[4] : ss0[4].doubleValue();
			tem[5] = (ss0[5] == null) ? base[5] : ss0[5].doubleValue();
			tem[6] = (ss0[6] == null) ? base[6] : ss0[6].doubleValue();
			// p.p(tem[3]+" ??  "+base[3]);

			difDS[i] = tem[3] - tem[4] - tem[5] + tem[6]
					- (base[3] - base[4] - base[5] + base[6]);
			if (i == 0)
				difDS[i] = 0;
			difDSv = difDSv + difDS[i];
			difDSr[i] = difDS[i] / (tem[0] * tem[2]);

			difD[i] = tem[3] - tem[4] - (base[3] - base[4]);
			if (i == 0)
				difD[i] = 0;
			difDv = difDv + difD[i];
			difDr[i] = difD[i] / (tem[0] * tem[2]);
			// dif[i]=tem[3]-tem[4]-tem[5]+tem[6]+(base[3]-base[4]-base[5]+base[6]);
			// difv=difv+dif[i];

			amountsD[i] = tem[3] + tem[4];

			fan[i] = (ss0[7] == null) ? base[7] : ss0[7].doubleValue();
			if (Math.abs(fan[i]) > 10000)
				fan[i] = 0;

			if ((tem[3] + tem[4] + tem[5] + tem[6]) == 0)
				fanD[i] = 0;
			else
				fanD[i] = fan[i] * difD[i]
						/ (tem[3] + tem[4] + tem[5] + tem[6]);

			base[3] = tem[3];
			base[4] = tem[4];
			base[5] = tem[5];
			base[6] = tem[6];
			// if(Math.abs(fanD[i])>10000)
			// fanD[i]=0;
			// / p.p(fan[i]+"  "+fanD[i]+"  "+difD[i]+" ??  "+base[3]);

			// tem[8]=(ss0[3]==null)? base[8]:ss0[8].doubleValue();
			// tem[9]=(ss0[4]==null)? base[9]:ss0[9].doubleValue();
			tem[10] = (ss0[10] == null) ? base[10] : ss0[10].doubleValue();
			tem[11] = (ss0[11] == null) ? base[11] : ss0[11].doubleValue();
			caibuyR[i] = tem[10] / (tem[0] * tem[2]);
			caisellR[i] = tem[11] / (tem[0] * tem[2]);
			base[7] = fan[i];
			base[8] = tem[8];
			base[9] = tem[9];
			base[10] = tem[10];
			base[11] = tem[11];

			tem[12] = (ss0[12] == null) ? base[12] : ss0[12].doubleValue();
			shoubiD[i] = tem[12];
			// p.p(tem[12]+"  "+base[12]);

			base[12] = tem[12];

			tem[13] = (ss0[13] == null) ? base[13] : ss0[13].doubleValue();
			priceW[i] = tem[13];
			base[13] = tem[13];

			tem[14] = (ss0[14] == null) ? base[14] : ss0[14].doubleValue();
			priceDW[i] = tem[14];
			base[14] = tem[14];

			tem[15] = (ss0[15] == null) ? base[15] : ss0[15].doubleValue();
			VirP[i] = tem[15];
			base[15] = tem[15];

		}

		double[] ap = this.getSS(prices, amounts);
		double[] apD = this.getSS(priceW, amountsD);

		String[] result = new String[23];
		if (db.isZiSu(code)) {
			result[0] = "" + price;
			result[1] = "" + amount;
			result[2] = "" + difDSv;
			result[3] = "" + getSum(difDSr);
			result[4] = "" + difDv;
			result[5] = "" + getSum(difDr);
			result[6] = "" + getSum(fan);
			result[7] = "" + getSum(fanD);
			result[8] = "0";
			result[9] = "0";
			result[10] = "0";// +getJFBY(shoubi);
			result[11] = "0";
			result[12] = "0";// +getJFBY(shoubi);
			result[13] = "0";
			result[14] = "" + ap[0];// h
			result[15] = "" + ap[1];//
			result[16] = "" + ap[2];
			result[17] = "" + ap[3];
			result[18] = "" + ap[4];
			result[19] = "" + ap[5];
			result[20] = "" + ap[6];
			result[21] = "" + ap[7];
			result[22] = "" + ap[8];
			// result[14]=priceW;//price by weight

		} else {
			result[0] = "" + price;
			result[1] = "" + amount;
			result[2] = "" + difDSv;
			result[3] = "" + getSum(difDSr);
			result[4] = "" + difDv;
			result[5] = "" + getSum(difDr);
			result[6] = "" + getSum(fan);
			result[7] = "" + getSum(fanD);
			result[8] = "" + getAverage(caibuyR);
			result[9] = "" + getAverage(caisellR);
			result[10] = "" + getJFBY(shoubi);
			result[11] = "" + getQS(shoubi);
			result[12] = "" + getJFBY(shoubiD);
			result[13] = "" + getQS(shoubiD);
			result[14] = "" + ap[0];// h
			result[15] = "" + ap[1];//
			result[16] = "" + ap[2];
			result[17] = "" + ap[3];
			result[18] = "" + ap[4];
			result[19] = "" + ap[5];
			result[20] = "" + ap[6];
			result[21] = "" + ap[7];
			result[22] = "" + ap[8];
			// result[14]=priceW;//price by weight
		}
		/*
		 * String s=""; for(int i=0;i<result.length;i++) { s=s+result[i]+" | ";
		 * } p.p(s+"-----"+code); //
		 */
		return result;

	}

	private double[] getSS(double[] prices, double[] amounts) {
		double value = 0, valuea = 0, high = prices[0], low = prices[0], posPL, posAL, posPH, posAH, ahigh = amounts[0], alow = amounts[0], avalue = 0, avalueW = 0;
		for (int i = 0; i < prices.length; i++) {
			if (prices[i] > high)
				high = prices[i];
			if (prices[i] <= low)
				low = prices[i];
			if (amounts[i] > ahigh)
				ahigh = amounts[i];
			if (amounts[i] <= low)
				alow = amounts[i];
			value = value + prices[i];
			valuea = valuea + amounts[i];
			avalue = avalue + prices[i] * amounts[i];
			avalueW = avalueW + amounts[i];
		}
		value = value / prices.length;
		valuea = valuea / prices.length;
		posPL = (prices[prices.length - 1] - low) / low;
		posAL = (amounts[prices.length - 1] - alow) / alow;
		posPH = (high - prices[prices.length - 1]) / high;
		posAH = (ahigh - amounts[prices.length - 1]) / ahigh;
		double cost = avalue / avalueW;// jia jun pin jun
		double[] result = new double[9];
		result[0] = high;
		result[1] = low;
		result[2] = ahigh;
		result[3] = alow;
		result[4] = cost;
		result[5] = posPL;
		result[6] = posAL;
		result[7] = posPH;
		result[8] = posAH;
		return result;
	}

	private double getJFBY(double[] data) {
		// p.p("81 sql=?"+Math.sqrt(81));
		double tem = 0, tem2 = 0, tem3 = 0;
		for (int i = 0; i < data.length; i++) {
			tem = tem + data[i];
		}
		for (int i = 0; i < data.length; i++) {
			tem2 = tem2 + (data[i] - tem / data.length)
					* (data[i] - tem / data.length);
		}

		tem3 = Math.sqrt(tem2 / data.length);

		return tem3 / (tem / data.length);

	}

	private double getQS(double[] data) {
		double value = 0, a = 0, b = 0, c = 0, standard = 0, A = 0, B = 0;
		double n = 200;
		double h = 1000;
		for (int i = 0; i < data.length; i++) {
			b = b + data[i];
		}
		b = b / data.length;

		for (double i = (0 - n / 2); i < (n / 2); i++) {

			for (double j = (0 - h / 2); j < (h / 2); j++) {
				a = j * 24 / h;
				c = b * (1 + i * 2 / n / 3);
				for (int m = 0; m < data.length; m++) {
					value = value + Math.abs(data[m] - (a * m + c));
				}
				if (standard == 0)
					standard = value;
				// p.p(""+a);
				if (value < standard) {
					standard = value;
					A = a;
					B = c;
					// p.p("A="+A+"  a="+a);
				}

				// if(j==-80)
				// p.p("value="+value+"  A="+A+"  "+standard+"   a="+a);
				value = 0;
			}
		}
		return A;
	}

	private double[] getBaseV(Vector data) {
		Double[] ss0 = null;
		double[] base = new double[((Double[]) data.get(0)).length];
		Vector tem = new Vector();
		int i = 0;
		while (true) {
			ss0 = (Double[]) data.get(i);

			if (i == 0) {
				for (int j = 0; j < ss0.length; j++) {
					if (ss0[j] == null)
						tem.add("" + j);
					else
						base[j] = ss0[j].doubleValue();
				}
				if (tem.size() == 0)
					break;
				i++;
				continue;
			}
			for (int j = 0; j < tem.size(); j++) {
				int n = Integer.parseInt((String) (tem.get(j)));
				if (ss0[n] == null)
					continue;
				else {
					base[j] = ss0[n].doubleValue();
					tem.removeElement("" + n);
				}
			}
			if (tem.size() == 0)
				break;
			i++;
			if (i > data.size() - 1)
				break;
		}
		return base;
	}

	private double getSum(double[] datan) {
		double sum = 0;
		for (int i = 0; i < datan.length; i++) {
			sum = sum + datan[i];
		}
		return sum;
	}

	private double getAverage(double[] datan) {
		double sum = 0;
		for (int i = 0; i < datan.length; i++) {
			sum = sum + datan[i];
		}
		return sum / datan.length;
	}

	public String[] toDatabase(String code, java.sql.Date ndate) {
		// code="600688";
		data = db.getDetails(code, ndate);
		double pFactor = db.getPriceW(code);
		if (data == null || data.size() == 0) {
			display.setText("No data");
			return null;
		}
		String[] ss = null;
		float circleGu = db.getLiaoTong(code) * 10000;
		float zhub = 0, zhus = 0, sanb = 0, sans = 0, value = 0, totalShou = 0, totalPrice = 0, shouv, priceW = 0, priceDW = 0;
		long totalBi = 0;
		for (int i = 0; i < data.size(); i++) {
			ss = (String[]) data.get(i);
			totalShou = totalShou + Float.parseFloat(ss[2]);
			totalPrice = totalPrice + Float.parseFloat(ss[1]);
			priceW = priceW + Float.parseFloat(ss[1]) * Float.parseFloat(ss[2]);
			totalBi++;
		}

		float priceA = totalPrice / totalBi;// ok Y/gu
		priceW = priceW / totalShou;
		float shouA = totalShou / totalBi;
		float moneyA = priceA * totalShou * 100 / totalBi;
		float circleRate = totalShou * 100 / circleGu;
		if (circleGu == 0)
			circleRate = 0;

		float shouAD = 0;
		int dbi = 0;
		for (int i = 0; i < data.size(); i++) {
			ss = (String[]) data.get(i);
			value = Float.parseFloat(ss[3]);
			shouv = Float.parseFloat(ss[2]);
			// p.p(code+":  value="+value+"   MoneyA="+moneyA);
			if (value > moneyA) {
				if (ss[4].equalsIgnoreCase("+"))
					zhub = zhub + shouv;// shou
				else
					zhus = zhus + shouv;// shou
				priceDW = priceDW + Float.parseFloat(ss[1])
						* Float.parseFloat(ss[2]);

			} else {

				if (ss[4].equalsIgnoreCase("+"))
					sanb = sanb + shouv;
				else
					sans = sans + shouv;
			}

			if (Float.parseFloat(ss[2]) > shouA) {
				shouAD = shouAD + Float.parseFloat(ss[2]);
				dbi++;
			}

		}

		shouAD = shouAD / dbi;
		priceDW = priceDW / (zhub + zhus);
		Vector prices = getPrices();
		Vector olddata = getGuaData(prices);
		Vector ndata = this.getAdd(prices, olddata);
		Vector datan = this.getGuaDetail(ndata, prices);
		float gb = 0, gs = 0, cb = 0, cs = 0;
		for (int i = 0; i < datan.size(); i++) {
			ss = (String[]) datan.get(i);
			value = Float.parseFloat(ss[1]);
			// p.p(value+"?="+(moneyA/Float.parseFloat(ss[2])*100));
			if (Math.abs(value * (Float.parseFloat(ss[2]) * 100)) > moneyA) {
				if (value > 0) {
					if (ss[3].equalsIgnoreCase("+"))
						gb = gb + value;
					else
						gs = gs + value;

				} else {

					if (ss[3].equalsIgnoreCase("+"))
						cb = cb + value;
					else
						cs = cs + value;
				}
			}

		}

		String[] result = new String[16];
		if (db.isZiSu(code)) {
			result[0] = "" + totalBi;
			result[1] = "" + moneyA;
			result[2] = "" + shouA;
			result[3] = "" + zhub;
			result[4] = "" + zhus;
			result[5] = "" + sanb;
			result[6] = "" + sans;
			result[7] = "0";
			result[8] = "" + gb;
			result[9] = "" + gs;
			result[10] = "" + cb;
			result[11] = "" + cs;
			result[12] = "" + shouAD;
			result[13] = "" + priceW;
			result[14] = "" + priceDW;
			result[15] = "" + moneyA / shouA / 100 * pFactor;
		} else {
			result[0] = "" + totalBi;
			result[1] = "" + moneyA;
			result[2] = "" + shouA;
			result[3] = "" + zhub;
			result[4] = "" + zhus;
			result[5] = "" + sanb;
			result[6] = "" + sans;
			result[7] = "" + circleRate;
			result[8] = "" + gb;
			result[9] = "" + gs;
			result[10] = "" + cb;
			result[11] = "" + cs;
			result[12] = "" + shouAD;
			result[13] = "" + priceW;
			result[14] = "" + priceDW;
			result[15] = "" + moneyA / shouA / 100 * pFactor;
		}
		// p.p(""+(float)pFactor+"   "+result[15]);

		if (code.equalsIgnoreCase("1A0001"))
			db.priceSH = moneyA / shouA / 100;
		if (code.equalsIgnoreCase("399001"))
			db.priceSZ = moneyA / shouA / 100;
		// p.p(totalBi+","+moneyA+","+shouA+","+zhub+","+zhus+","+sanb+","+sans+","+gb+","+gs+","+cb+","+cs);
		return result;
	}

	private Vector getAdd(Vector prices, Vector olddata) {
		Vector ndata = new Vector();
		int len = ((String[]) (olddata.get(0))).length;
		String[] d0 = new String[len];
		String[] d = null;
		int mai = 0;
		float long1, long2;
		String s1 = "", s2 = "";
		for (int i = 0; i < olddata.size(); i++) {
			d = (String[]) (olddata.get(i));
			mai = Integer.parseInt(d[len - 1]);
			if (i == 0) {
				ndata.add(d);
				d0 = (String[]) d.clone();
				continue;
			} else {

				for (int j = 4; j < d.length - 1; j++) {
					if (d[j] == null || d[j].length() == 0)
						continue;

					s1 = (d0[j] == null || d0[j].length() == 0) ? "0" : d0[j];
					s2 = (d[j] == null || d[j].length() == 0) ? "0" : d[j];
					long1 = Float.parseFloat(s2) - Float.parseFloat(s1);
					
					d0[j] = d[j];
					d[j] = "" + long1;

				}

				int tem = getIndex(prices, d[1]);// to locate position of sold
				
				if (d[tem + 4] == null || d[tem + 4].length() == 0)
					d[tem + 4] = "0";
				d[tem + 4] = ""
						+ (Float.parseFloat(d[tem + 4]) + Float
								.parseFloat(d[2]));
				ndata.add(d);

			}
		}
		return ndata;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == detail) {
			detailOp();
			return;
		}
		if (source == selection) {
			selectionOp();
			return;
		}
		if (source == jiajian) {
			jiajianOp();
			return;
		}
		if (source == shous) {
			shousOp();
			return;
		}
		if (source == fdetail) {
			fdetailOp();
			return;
		}
		if (source == addition) {
			additionOp();
			return;
		}
		if (source == step2) {
			step2Op();
			return;
		}
	}

	private void step2Op() {

		String[] columnNames = new String[] { "Code", "PriceV", "AmountV",
				"DifDS", "DifDSr", "DifD", "DifDr", "Fan", "FanD", "CBr",
				"CSr", "JFBY", "QS", "DJFBY", "DQS", "H", "L", "AH", "AL",
				"Cost", "PPL", "APL", "PPH", "APH", "Date", "Days" };
		
		model.setNames(columnNames);
		data = db.getCalculation(null);
		model.setID("Step2");
		model.setData(data);

	}

	private Vector getGuaDetail(Vector olddata, Vector prices) {
		String[] sso, ssn;
		Vector ndata = new Vector();
		for (int i = 0; i < olddata.size(); i++) {
			sso = (String[]) (olddata.get(i));
			for (int j = 4; j < sso.length - 1; j++) {
				if (sso[j] != null && sso[j].length() != 0
						&& Float.parseFloat(sso[j]) != 0) {
					ssn = new String[4];
					ssn[0] = sso[0];
					ssn[1] = sso[j];
					ssn[2] = "" + ((Float) (prices.get(j - 4))).floatValue();
					int index = Integer.parseInt(sso[sso.length - 1]);
					ssn[3] = (j >= index) ? "-" : "+";

					ndata.add(ssn);
				}
			}

		}
		return ndata;
	}

	private void additionOp() {
		Vector prices = getPrices();
		Vector olddata = getGuaData(prices);
		Vector ndata = this.getAdd(prices, olddata);
		Vector datan = this.getGuaDetail(ndata, prices);
		String[] names = new String[4];
		names[0] = "Time";
		names[1] = "Shou";
		names[2] = "Price";
		names[3] = "Type";
	
		model.setNames(names);
		model.setID("Addition");
		model.setData(datan);

	}

	private void detailOp() {
		java.sql.Date s = (java.sql.Date) (date.getSelectedItem());
		String[] columnNames = new String[] { "Time", "Price", "Shou",
				"Amount", "Type" };
		
		model.setNames(columnNames);
		data = db.getDetails((String) (ids.getSelectedItem()), s);
		model.setID("Detail");
		model.setData(data);
	}

	private void selectionOp() {
		java.sql.Date s = (java.sql.Date) (date.getSelectedItem());
		String[] columnNames = new String[] { "Code", "DifDS", "DifDAR", "CR",
				"CB", "CS", "CBR", "CSR", "Name", "BB", "BS", "SB", "SS",
				"TBI", "SBI", "VP" };
		
		model.setNames(columnNames);
		data = db.getSelection(s);
		model.setID("Selection");
		model.setData(data);
	}

	private void shousOp() {
		
		if (!stop)
			stop = true;
		if (runID.equalsIgnoreCase("NA")) {
			stop = false;
			display.setText("Starting");
		}
		runID = "CAL0";
		// */
	}

	private void Cal0() {
		
		java.sql.Date now = (java.sql.Date) (date.getSelectedItem());
		display.setText(now.toString());
		String code = "";
		Vector adata = new Vector();
		Vector udata = new Vector();
		Vector acode = new Vector();
		Vector ucode = new Vector();
		String[] tem = null;

		int size = ids.getItemCount();
		int count = 0;
		for (int i = 0; i < size; i++) {
			count++;
			code = (String) (ids.getItemAt(i));
			tem = this.toDatabase(code, now);
			if (tem != null) {
				display.setText(count + "/" + size + " :" + code + "  P");
				if (db.check(code, now)) {
					ucode.add(code);
					udata.add(tem);
				} else {
					acode.add(code);
					adata.add(tem);
				}
			}
			if (stop) {
				display.setText("Stopped");
				return;
			}
		}
		if (udata != null)
			db.updateStock(ucode, now, udata);
		if (adata != null)
			db.addStock(acode, now, adata);
		// }
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			dispose();
			System.exit(0);
		}
		super.processWindowEvent(e);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Main mainS = new Main();
		Thread thread1 = new Thread(mainS);
		thread1.start();
	}

	private void jbInit() throws Exception {
		// this.getContentPane().setLayout(xYLayout1);
		customerPane.setBorder(BorderFactory.createLineBorder(new Color(146,
				179, 171), 1));
		// customerPane.setLayout(xYLayout3);
		// xYLayout1.setWidth(962);
		// xYLayout1.setHeight(484);
		// control_cus.setLayout(xYLayout2);
		jLabel1_cus.setText("Restaurant ID:");
		jLabel1_cus.setHorizontalAlignment(SwingConstants.TRAILING);
		detail.setText("Detail");
		jiajian.setText("JiaJian");
		jLabel1_cus1.setText("Customer ID:");
		jLabel1_cus1.setHorizontalAlignment(SwingConstants.TRAILING);
		display.setHorizontalAlignment(SwingConstants.TRAILING);
		shous.setText("Shous");
		selection.setText("Step1");
		fdetail.setText("F_Detail");
		addition.setText("GetAdd");
		step2.setText("Step2");
		/*
		 * / control_cus.add(step2, new XYConstraints(520, 45, 73, 23));
		 * control_cus.add(addition, new XYConstraints(430, 45, 73, 23));
		 * control_cus.add(fdetail, new XYConstraints(315, 45, 75, 25));
		 * control_cus.add(selection, new XYConstraints(195, 45, 100, 30));
		 * control_cus.add(shous, new XYConstraints(35, 45, 120, 30));
		 * control_cus.add(dadan, new XYConstraints(720, 5, 90, 25));
		 * control_cus.add(display, new XYConstraints(720, 45, 210, 25));
		 * control_cus.add(jLabel1_cus1, new XYConstraints(395, 5, 85, 25));
		 * control_cus.add(ids, new XYConstraints(480, 5, 190, 25));
		 * control_cus.add(jiajian, new XYConstraints(295, 5, 100, 25));
		 * control_cus.add(detail, new XYConstraints(190, 5, 95, 25));
		 * control_cus.add(jLabel1_cus, new XYConstraints(5, 5, 80, 25));
		 * control_cus.add(date, new XYConstraints(85, 5, 105, 25));
		 * customerPane.add(control_cus, new XYConstraints(0, 0, 940, 80));
		 * jScrollPane6_cus.getViewport().add(table, null);
		 * customerPane.add(jScrollPane6_cus, new XYConstraints(0, 85, 940,
		 * 355)); this.getContentPane().add(customerPane, new XYConstraints(5,
		 * 5, 940, 440)); //
		 */
		jScrollPane6_cus.setPreferredSize(new Dimension(1500, 800));
		// table.setPreferredSize(new Dimension(1500,100));
	}

	private void customer1Button_actionPerformed(ActionEvent e) {
	}

	private void disableIt(Component c) {
		c.setEnabled(false);
	}

	private void able(Component c) {
		c.setEnabled(true);
	}

	private void actionListener(JButton c) {
		c.addActionListener(this);
	}

	private void actionListener(JComboBox c) {
		c.addActionListener(this);
	}

	private void actionListener(JRadioButton c) {
		c.addActionListener(this);
	}

}