package stock;

/* Author: Zule Li
 * Email:zule.li@hotmail.com
 * Last Modified Date:Mar.7,2015
 * */

//for Stock project
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class Database extends Object {

	private String source = "jdbc:odbc:Stock";
	private Connection con;
	public static double priceSH = 0;
	public static double priceSZ = 0;
	private Main me;

	/**
	 * Constructor
	 */
	public Database(Main me) {
		this.me = me;
		/*
		 * / try { Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		 * 
		 * } catch(ClassNotFoundException e) { e.printStackTrace(); } //
		 */
	}

	public void systemUpdate(java.sql.Date date) {
		Vector v = new Vector();
		Vector cv = this.getStockIDS();
		String[] ssd = new String[2], ss = null;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "select * from gpFenbi where theDate=?";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setDate(1, date);

			rs = ste.executeQuery();

			boolean status = true;
			while (rs.next()) {
				ss = new String[3];
				ss[0] = rs.getString(1);
				ss[1] = rs.getString(2);
				

			}

			rs.close();
			ste.close();
			if (v.size() != 0) {
				str = "insert into CodeName values (?,?,?,?,?)";
				ste = con.prepareStatement(str);
				for (int i = 0; i < v.size(); i++) {
					ste.setString(1, ((String[]) v.get(i))[0]);
					ste.setString(2, ((String[]) v.get(i))[1]);
					ste.setString(3, "Unknown");
					ste.setBoolean(4, false);
					ste.setDouble(5, 0);
					int index = ste.executeUpdate();
				}
			}
			ste.close();
			con.close();

			P.p("FINISH");

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}

	}

	public void systemUpdate(Vector data, Vector codes) {

		String[] ssd = new String[3];
		Vector ids = this.getStockIDS();
		String shichang = "NA";
		String code = "000696";
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "update CodeName set priceW=? where code=?";
			PreparedStatement ste = con.prepareStatement(str);
			for (int i = 0; i < data.size(); i++) {
				// *
				ssd = (String[]) data.get(i);
				code = (String) codes.get(i);
				for (int j = 0; j < ids.size(); j++) {
					if ((((String[]) (ids.get(j)))[0]).equalsIgnoreCase(code)) {
						shichang = ((String[]) (ids.get(j)))[2];
						P.p(code + ": ");
						break;
					}

				}
				
				ste.setDouble(1, (priceSH / (Double.parseDouble(ssd[1])
						/ Double.parseDouble(ssd[2]) / 100 * 100)));
				
				ste.setString(2, code);
				int index = ste.executeUpdate();

			}
			ste.close();
			con.close();

			P.p("FINISH");

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}

	}

	public Vector getDetails(String id, java.sql.Date date) {
		
		String[] ss = new String[18];
		Vector data = new Vector();
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "select * from gpFenbi where (theDate=? and code=?)";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setDate(1, date);
			ste.setString(2, id);

			rs = ste.executeQuery();
			

			while (rs.next()) {
				ss = new String[19];
				ss[0] = (rs.getTime("JDate")).toString();
				ss[1] = "" + rs.getFloat("Pnow");
				ss[2] = "" + rs.getFloat("amount");
				ss[3] = "" + rs.getFloat("money");
				ss[4] = rs.getBoolean("AZhuDong") ? "-" : "+";
				// *
				ss[5] = "" + rs.getFloat("a1");
				ss[6] = "" + rs.getFloat("a2");
				ss[7] = "" + rs.getFloat("a3");
				ss[8] = "" + rs.getFloat("v1");
				ss[9] = "" + rs.getFloat("v2");
				ss[10] = "" + rs.getFloat("v3");
				ss[11] = "" + rs.getFloat("ap1");
				ss[12] = "" + rs.getFloat("ap2");
				ss[13] = "" + rs.getFloat("ap3");
				ss[14] = "" + rs.getFloat("vp1");
				ss[15] = "" + rs.getFloat("vp2");
				ss[16] = "" + rs.getFloat("vp3");
				ss[17] = "" + rs.getFloat("Nei");
				ss[18] = "" + rs.getFloat("Wai");

				data.add(ss);
			}

			rs.close();
			ste.close();
			con.close();
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;

	}

	public boolean updateRestaurant(String[] ss) {

		/**
		 * 0=accountNO,1-first,2=last,3=street,4=apt,5=city,6=state,7=zip,8=
		
		 */
		boolean ok = false;
		try {
			con = DriverManager.getConnection(source);
			String str = "update Restaurants set businessName=?,street=?"
					+ ",unit=?,city=?,state=?,zip=?,phone=?,ABName=?,firstName=?,lastName=?,cellPhone=?,phone3=? where restaurantID=?";

			PreparedStatement ste = con.prepareStatement(str);

			ste.setString(1, ss[1]);
			ste.setString(2, ss[2]);
			ste.setString(3, ss[3]);
			ste.setString(4, ss[4]);
			ste.setString(5, ss[5]);
			ste.setString(6, ss[6]);
			ste.setString(7, ss[7]);
			ste.setString(8, ss[8]);
			ste.setString(9, ss[9]);
			ste.setString(10, ss[10]);
			ste.setString(11, ss[11]);
			ste.setString(12, ss[12]);
			ste.setString(13, ss[0]);

			int ind = ste.executeUpdate();

			if (ind > 0) {
				ok = true;
			}
			ste.close();
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return ok;

	}

	public String deleteItem(String[] itid, String[] resid) {
		String ok = null;
		try {
			con = DriverManager.getConnection(source);
			// 0-id,1=title,2=version,3=location,4=catagory,5-publisher,6=summary,7=createDay
			String str = "delete from menu  where (restaurantid=? and itemid=?)";
			PreparedStatement ste = con.prepareStatement(str);
			for (int i = 0; i < itid.length; i++) {
				ste.setString(1, resid[i]);
				ste.setString(2, itid[i]);
				int ind = ste.executeUpdate();
				if (ind <= 0) {
					if (ok == null)
						ok = "The Following item failed to delete from database:\nRestaurant ID:"
								+ resid[i] + ", ItemID:" + itid[i];
					else
						ok = ok + "\nRestaurant ID:" + resid[i] + ", ItemID:"
								+ itid[i];
				}
			}
			ste.close();
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return ok;
	}

	public String deleteRestaurant(String[] itid) {
		String ok = null;
		try {
			con = DriverManager.getConnection(source);
			// 0-id,1=title,2=version,3=location,4=catagory,5-publisher,6=summary,7=createDay
			String str = "delete from Restaurants  where restaurantid=?";
			System.out.println(str);
			PreparedStatement ste = con.prepareStatement(str);
			for (int i = 0; i < itid.length; i++) {
				ste.setString(1, itid[i]);
				int ind = ste.executeUpdate();
				if (ind < 0) {
					if (ok == null)
						ok = "The Following item failed to delete from database:\nRestaurant ID:"
								+ itid[i];
					else
						ok = ok + "\nRestaurant ID:" + itid[i];
				}
			}
			ste.close();
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return ok;
	}

	public boolean updateStock(Vector codes, java.sql.Date ndate, Vector data) {
		P.p("u");
		
		try {
			con = DriverManager.getConnection(source);
			String str = "update results set totalBi=?,MoneyBi=?,ShouBi=?"
					+ ",bigbuy=?,bigsell=?,smallbuy=?,smallsell=?,fanshourate=?"
					+ ",guabuy=?,guasell=?,caibuy=?,caisell=?,ShouBiD=?,PriceW=?,PriceDW=?,VirtualPrice=? where (code=? and Tdate=?)";
			PreparedStatement ste = con.prepareStatement(str);

			String[] ss = new String[15];
			int size = data.size(), count = 0;
			String code = "";
			for (int i = 0; i < size; i++) {
				ss = (String[]) data.get(i);
				ste.setDouble(1, Double.parseDouble(ss[0]));
				ste.setDouble(2, Double.parseDouble(ss[1]));
				ste.setDouble(3, Double.parseDouble(ss[2]));
				ste.setDouble(4, Double.parseDouble(ss[3]));
				ste.setDouble(5, Double.parseDouble(ss[4]));
				ste.setDouble(6, Double.parseDouble(ss[5]));
				ste.setDouble(7, Double.parseDouble(ss[6]));
				ste.setDouble(8, Double.parseDouble(ss[7]));
				ste.setDouble(9, Double.parseDouble(ss[8]));
				ste.setDouble(10, Double.parseDouble(ss[9]));
				ste.setDouble(11, Double.parseDouble(ss[10]));
				ste.setDouble(12, Double.parseDouble(ss[11]));
				ste.setDouble(13, Double.parseDouble(ss[12]));
				ste.setDouble(14, Double.parseDouble(ss[13]));
				ste.setDouble(15, Double.parseDouble(ss[14]));
				ste.setDouble(16, Double.parseDouble(ss[15]));

				code = (String) codes.get(i);
				ste.setString(17, code);
				ste.setDate(18, ndate);
				int ind = ste.executeUpdate();
				if (ind < 0) {
					count++;
					me.display.setText(count + "/" + size + " : " + code
							+ "  U_F");
					continue;

				}
				count++;
				me.display.setText(count + "/" + size + " : " + code + "  U_T");
			}
			ste.close();
			con.close();
			me.display.setText("Finished");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			me.display.setText("Error");

		}
		return false;

	}

	public String[] getMeasueUpdate(String[] ss) {
		String[] out = new String[3];
		try {
			con = DriverManager.getConnection(source);
			String str = "select((select capacity/(RO*RO-RI*RI)*((RO-measurement)*(RO-measurement)-RI*RI) from emptybeam where size=beamsize)/end*453.6/"
					+ "(select Rawm.size*Rawm.modify from Rawm where Rawm.stock=Measurement.stock)*9000/"
					+ "((select (speed*efficiency/100) from MCS where MCS.MC=measurement.mc)/"
					+ "(SELECT pick from styles where styles.styleno=(select styleno from mcs where MCS.MC=measurement.mc))/36*0.9199*60)),"
					+

					"(select sum([status])from beams where(status=1 and beams.stock=measurement.stock and beams.end=measurement.end)),"
					+

					"(select capacity/(RO*RO-RI*RI)*((RO-measurement)*(RO-measurement)-RI*RI) from emptybeam where size=beamsize)/end*453.6/"
					+ "(select Rawm.size*Rawm.modify from Rawm where Rawm.stock=Measurement.stock)*9000*0.9199*"
					+ "(SELECT (pick/Fpick) from styles where styles.styleno=(select styleno from mcs where MCS.MC=measurement.mc))"
					+

					" from measurement where (mc='"
					+ ss[0]
					+ "' and position="
					+ ss[1] + ")";
			Statement ste = con.createStatement();
			ResultSet rs = ste.executeQuery(str);
			if (rs.next()) {

				long lon = (long) (rs.getDouble(1));
				out[0] = ""
						+ (new java.sql.Date(System.currentTimeMillis() + lon
								* 60 * 60 * 1000)).toString();// dateOUt
				out[1] = "" + rs.getInt(2);
				out[2] = "" + (int) (rs.getDouble(3));
			}
			ste.close();
			con.close();
			return out;
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return null;

	}

	public String setInActive(String[] ss) {
		String ok = null;
		try {
			con = DriverManager.getConnection(source);
			String str = "update customers set status='InActive' where accountno=?";
			PreparedStatement ste = con.prepareStatement(str);
			for (int i = 0; i < ss.length; i++) {
				ste.setString(1, ss[i]);
				int ind = ste.executeUpdate();
				if (ind < 0) {
					if (ok == null)
						ok = "The Following item failed to set InActive from database:\nAccount#:";
					else
						ok = ok + "\nAccount#:" + ss[i];
				}
			}
			ste.close();
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return ok;
	}

	public boolean setLocalPort(String s) {
		boolean result = false;
		try {
			con = DriverManager.getConnection(source);

			String str = "update locals set localPort=?";
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, s);
			int ind = ste.executeUpdate();
			if (ind > 0) {
				result = true;
			}
			ste.close();
			con.close();

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return result;
	}

	
	public float getLiaoTong(String id) {

		float s = 0;

		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "select F7 from Fin where code=?";
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, id);
			rs = ste.executeQuery();
			if (rs.next()) {

				s = rs.getFloat("F7");
				rs.close();
				ste.close();
				con.close();
				return s;
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return s;
	}

	public long getNextValue(String id, String resid) {

		long s = 0;

		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "select * from NextValue where restaurantid=?";
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, resid);

			rs = ste.executeQuery();
			if (rs.next()) {

				s = (long) (rs.getDouble(id));
				rs.close();
				ste.close();
				con.close();
				P.p("resid=" + resid + " s=" + s + "  id=" + id);
				return s;
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return s;
	}

	public Vector getCalculation(String id) {
		String[] ss = new String[18];
		Vector data = new Vector();
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "";
			if (id != null)
				str = "select * from calculation where code=?";
			else
				str = "select * from calculation";

			PreparedStatement ste = con.prepareStatement(str);
			if (id != null)
				ste.setString(1, id);

			rs = ste.executeQuery();
			/*
			 * 
			 * 
			 * //
			 */

			while (rs.next()) {
				ss = new String[26];
				ss[0] = rs.getString("Code");
				ss[1] = "" + rs.getFloat(4);
				ss[2] = "" + rs.getFloat(5);
				ss[3] = "" + rs.getFloat(6);
				ss[4] = "" + rs.getFloat(7);
				ss[5] = "" + rs.getFloat(8);
				ss[6] = "" + rs.getFloat(9);
				ss[7] = "" + rs.getFloat(10);
				ss[8] = "" + rs.getFloat(11);
				ss[9] = "" + rs.getFloat(12);
				ss[10] = "" + rs.getFloat(13);
				ss[11] = "" + rs.getFloat(14);
				ss[12] = "" + rs.getFloat(15);
				ss[13] = "" + rs.getFloat(16);
				ss[14] = "" + rs.getFloat(17);
				ss[15] = "" + rs.getFloat(18);
				ss[16] = "" + rs.getFloat(19);
				ss[17] = "" + rs.getFloat(20);
				ss[18] = "" + rs.getFloat(21);
				ss[19] = "" + rs.getFloat(22);
				ss[20] = "" + rs.getFloat(23);
				ss[21] = "" + rs.getFloat(24);
				ss[22] = "" + rs.getFloat(25);
				ss[23] = "" + rs.getFloat(26);
				ss[24] = "" + (rs.getDate(2)).toString();
				ss[25] = "" + rs.getInt(3);
				data.add(ss);
			}

			rs.close();
			ste.close();
			con.close();
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;

	}

	public void addCalculation(Vector codes, java.sql.Date ndate, Vector data,
			int days) {

		String str = "insert into Calculation  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = DriverManager.getConnection(source);
			// con.
			PreparedStatement pst = con.prepareStatement(str);
			String[] ss = null;
			int size = data.size(), count = 0;
			String code = "";
			for (int i = 0; i < size; i++) {
				ss = (String[]) data.get(i);
				code = (String) codes.get(i);
				pst.setString(1, code);
				pst.setDate(2, ndate);
				pst.setInt(3, days);
				pst.setDouble(4, Double.parseDouble(ss[0]));
				pst.setDouble(5, Double.parseDouble(ss[1]));
				pst.setDouble(6, Double.parseDouble(ss[2]));
				pst.setDouble(7, Double.parseDouble(ss[3]));
				pst.setDouble(8, Double.parseDouble(ss[4]));
				pst.setDouble(9, Double.parseDouble(ss[5]));
				pst.setDouble(10, Double.parseDouble(ss[6]));
				pst.setDouble(11, Double.parseDouble(ss[7]));
				pst.setDouble(12, Double.parseDouble(ss[8]));
				pst.setDouble(13, Double.parseDouble(ss[9]));
				pst.setDouble(14, Double.parseDouble(ss[10]));
				pst.setDouble(15, Double.parseDouble(ss[11]));
				pst.setDouble(16, Double.parseDouble(ss[12]));
				pst.setDouble(17, Double.parseDouble(ss[13]));
				pst.setDouble(18, Double.parseDouble(ss[14]));
				pst.setDouble(19, Double.parseDouble(ss[15]));
				pst.setDouble(20, Double.parseDouble(ss[16]));
				pst.setDouble(21, Double.parseDouble(ss[17]));
				pst.setDouble(22, Double.parseDouble(ss[18]));
				pst.setDouble(23, Double.parseDouble(ss[19]));
				pst.setDouble(24, Double.parseDouble(ss[20]));
				pst.setDouble(25, Double.parseDouble(ss[21]));
				pst.setDouble(26, Double.parseDouble(ss[22]));

				int index = pst.executeUpdate();
				if (index < 0) {
					count++;
					me.display.setText(count + "/" + size + " : " + code
							+ "  A_F");
					continue;

				}
				count++;
				me.display.setText(count + "/" + size + " : " + code + "  A_T");
			}

			pst.close();
			con.close();
			me.display.setText("Finished");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			me.display.setText("Error");

		}

	}

	public void updateCalculation(Vector codes, java.sql.Date ndate,
			Vector data, int days) {

		String str = "update Calculation set TDate=?,NDay=?,PriceV=?,AmountV=?,DifDS=?"
				+ ",DifDSr=?,DifD=?,DifDr=?,FanR=?,FanDr=?"
				+ ",caibuyR=?,caisellR=?,ShouBiJFC=?,ShouBiQS=?,ShouBiDJFC=?,ShouBiDQS=?"
				+ ",high=?,low=?,ahigh=?,alow=?,PriceW=?,PricePositionL=?,AmountPositionL=?"
				+ ",PricePositionH=?,AmountPositionH=? where code=?";

		try {
			con = DriverManager.getConnection(source);

			PreparedStatement pst = con.prepareStatement(str);
			String[] ss = null;
			int size = data.size();
			int count = 0;
			String code = "";
			for (int i = 0; i < size; i++) {
				ss = (String[]) data.get(i);

				pst.setDate(1, ndate);
				pst.setInt(2, days);
				pst.setDouble(3, Double.parseDouble(ss[0]));
				pst.setDouble(4, Double.parseDouble(ss[1]));
				pst.setDouble(5, Double.parseDouble(ss[2]));
				pst.setDouble(6, Double.parseDouble(ss[3]));
				pst.setDouble(7, Double.parseDouble(ss[4]));
				pst.setDouble(8, Double.parseDouble(ss[5]));
				pst.setDouble(9, Double.parseDouble(ss[6]));
				pst.setDouble(10, Double.parseDouble(ss[7]));
				pst.setDouble(11, Double.parseDouble(ss[8]));
				pst.setDouble(12, Double.parseDouble(ss[9]));
				pst.setDouble(13, Double.parseDouble(ss[10]));
				pst.setDouble(14, Double.parseDouble(ss[11]));
				pst.setDouble(15, Double.parseDouble(ss[12]));
				pst.setDouble(16, Double.parseDouble(ss[13]));
				pst.setDouble(17, Double.parseDouble(ss[14]));
				pst.setDouble(18, Double.parseDouble(ss[15]));
				pst.setDouble(19, Double.parseDouble(ss[16]));
				pst.setDouble(20, Double.parseDouble(ss[17]));
				pst.setDouble(21, Double.parseDouble(ss[18]));
				pst.setDouble(22, Double.parseDouble(ss[19]));
				pst.setDouble(23, Double.parseDouble(ss[20]));
				pst.setDouble(24, Double.parseDouble(ss[21]));
				pst.setDouble(25, Double.parseDouble(ss[22]));
				code = (String) codes.get(i);
				pst.setString(26, code);
				int index = pst.executeUpdate();
				if (index < 0) {
					count++;
					me.display.setText(count + "/" + size + " : " + code
							+ "  U_F");
					continue;

				}
				count++;
				me.display.setText(count + "/" + size + " : " + code + "  U_T");

			}

			pst.close();
			con.close();
			me.display.setText("Finished");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			me.display.setText("Error");

		}

	}

	public boolean addStock(Vector codes, java.sql.Date ndate, Vector data) {

		String str = "insert into results  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		boolean ok = false;
		try {
			con = DriverManager.getConnection(source);
			PreparedStatement pst = con.prepareStatement(str);
			String[] ss = new String[16];
			int size = data.size(), count = 0;
			String code = "";
			for (int i = 0; i < size; i++) {
				
				ss = (String[]) data.get(i);
				code = (String) codes.get(i);
				pst.setString(1, code);
				pst.setDate(2, ndate);
				pst.setDouble(3, Double.parseDouble(ss[0]));
				pst.setDouble(4, Double.parseDouble(ss[1]));
				pst.setDouble(5, Double.parseDouble(ss[2]));
				pst.setDouble(6, Double.parseDouble(ss[3]));
				pst.setDouble(7, Double.parseDouble(ss[4]));
				pst.setDouble(8, Double.parseDouble(ss[5]));
				pst.setDouble(9, Double.parseDouble(ss[6]));
				pst.setDouble(10, Double.parseDouble(ss[7]));
				pst.setDouble(11, Double.parseDouble(ss[8]));
				pst.setDouble(12, Double.parseDouble(ss[8]));
				pst.setDouble(13, Double.parseDouble(ss[10]));
				pst.setDouble(14, Double.parseDouble(ss[11]));
				pst.setDouble(15, Double.parseDouble(ss[12]));
				pst.setDouble(16, Double.parseDouble(ss[13]));
				pst.setDouble(17, Double.parseDouble(ss[14]));
				pst.setDouble(18, Double.parseDouble(ss[15]));
				int index = pst.executeUpdate();
				if (index < 0) {
					count++;
					me.display.setText(count + "/" + size + " : " + code
							+ "  A_F");
					continue;

				}
				count++;
				me.display.setText(count + "/" + size + " : " + code + "  A_T");
			}

			pst.close();
			con.close();
			me.display.setText("Finished");
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			me.display.setText("Error");

		}
		return ok;
	}

	public boolean check(String code, java.sql.Date ndate) {
		boolean result = false;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;

			String str = "select * from results where (code=? and TDate=?)";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, code);
			ste.setDate(2, ndate);
			rs = ste.executeQuery();

			if (rs.next()) {
				result = true;

			}
			rs.close();
			ste.close();
			con.close();
			return result;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"unknown,database connection problem");

		}
		return true;
	}

	public boolean check(String code) {
		boolean result = false;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;

			String str = "select * from Calculation where code=?";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, code);
			rs = ste.executeQuery();

			if (rs.next()) {
				result = true;

			}
			rs.close();
			ste.close();
			con.close();
			return result;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"unknown,database connection problem");

		}
		return true;
	}

	public boolean isZiSu(String code) {
		boolean result = false;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;

			String str = "select * from CodeName where code=?";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, code);
			rs = ste.executeQuery();

			if (rs.next()) {
				result = rs.getBoolean("Status");

			}
			rs.close();
			ste.close();
			con.close();
			return result;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"unknown,database connection problem");

		}
		return false;
	}

	public double getPriceW(String code) {
		double result = 0;
		;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;

			String str = "select * from CodeName where code=?";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, code);
			rs = ste.executeQuery();

			if (rs.next()) {
				result = rs.getFloat("PriceW");

			}
			rs.close();
			ste.close();
			con.close();
			return result;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"unknown,database connection problem");

		}
		return -1;
	}

	public String getShiChang(String code) {
		String result = "";
		;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;

			String str = "select * from CodeName where code=?";

			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, code);
			rs = ste.executeQuery();

			if (rs.next()) {
				result = rs.getString("ShiChang");

			}
			rs.close();
			ste.close();
			con.close();
			return result;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"unknown,database connection problem");

		}
		return "Unknown";
	}

	public boolean checkForValid(String id, String type) {
		boolean result = false;
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "";
			if (type.equalsIgnoreCase("ItemID"))
				str = "select * from menu where itemid=?";
			if (type.equalsIgnoreCase("RestaurantID"))
				str = "select * from Restaurants where Restaurantid=?";
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, id);
			rs = ste.executeQuery();

			if (rs.next()) {
				result = true;

			}
			rs.close();
			ste.close();
			con.close();
			return result;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null, type
					+ " unknown,database connection problem");

		}
		return true;
	}

	public String[] getItem(String itemid, String resid) {
		String[] ss = new String[7];
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "select * from menu where (restaurantid=? and itemid=?)";
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, resid);
			ste.setString(2, itemid);

			rs = ste.executeQuery();
			// 0-id,1=title,2=version,3=location,4=catagory,5-publisher,6=summary,7=createDay,8=status,9=account
			if (rs.next()) {

				ss[0] = (rs.getString("ItemID")).trim();
				ss[1] = (rs.getString("Name")).trim();
				ss[2] = "" + rs.getDouble("Price");
				ss[3] = (rs.getString("Hot")).trim();
				ss[4] = (rs.getString("Catagory")).trim();
				ss[5] = (rs.getString("Status")).trim();
				ss[6] = (rs.getString("Description")).trim();
				rs.close();
				ste.close();
				con.close();
				return ss;
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return ss;

	}

	public boolean setGreetingMessage(String message) {
		boolean ok = false;
		try {
			con = DriverManager.getConnection(source);
			String str = "update NextValue set greeting=?";
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, message);
			int ind = ste.executeUpdate();
			if (ind > 0) {
				ok = true;
			}
			ste.close();
			con.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return ok;
	}

	public Vector getUsers() {

		return null;
	}

	public int getStep(String user, char[] psw) {

		return 0;
	}

	public Vector getRawdata(String code, java.sql.Date daten, int days) {

		Vector data = new Vector();
		Double[] ss = new Double[21];
		try {
			con = DriverManager.getConnection(source);
			String str = "select * from Results where (code=? and TDate=?)";
			PreparedStatement ste = con.prepareStatement(str);
			java.sql.Date date0 = new java.sql.Date(daten.getTime()
					- (days - 1) * 1000 * 24 * 60 * 60);

			ResultSet rs = null;

			for (int i = 0; i < days; i++) {
				ste.setString(1, code);
				ste.setDate(2, date0);
				rs = ste.executeQuery();
				if (rs.next()) {

					ss = new Double[16];

					ss[0] = Double.valueOf("" + rs.getDouble(3));// .trim();
					ss[1] = Double.valueOf("" + rs.getDouble(4));// .trim();
					ss[2] = Double.valueOf("" + rs.getDouble(5));// .trim();
					ss[3] = Double.valueOf("" + rs.getDouble(6));
					ss[4] = Double.valueOf("" + rs.getDouble(7));
					ss[5] = Double.valueOf("" + rs.getDouble(8));
					ss[6] = Double.valueOf("" + rs.getDouble(9));
					ss[7] = Double.valueOf("" + rs.getDouble(10));
					ss[8] = Double.valueOf("" + rs.getDouble(11));
					ss[9] = Double.valueOf("" + rs.getDouble(12));
					ss[10] = Double.valueOf("" + rs.getDouble(13));
					ss[11] = Double.valueOf("" + rs.getDouble(14));
					ss[12] = Double.valueOf("" + rs.getDouble(15));
					ss[13] = Double.valueOf("" + rs.getDouble(16));// .trim();
					ss[14] = Double.valueOf("" + rs.getDouble(17));// .trim();
					ss[15] = Double.valueOf("" + rs.getDouble(18));

					// p.p(date0.toString()+" :"+ss[12]);
					data.add(ss);

				}
				date0 = new java.sql.Date(date0.getTime() + 1000 * 24 * 60 * 60);
				// p.p(date0.toString());
				// p.p(date0.toString());
			}
			// if(total!=null)
			// total=Double.valueOf(""+to);
			// time=Integer.valueOf(""+ti);
			rs.close();
			ste.close();
			con.close();
			// p.p("dt="+total.doubleValue());
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;
	}

	public Vector getCustomers(String id, String cusid) {
		
		Vector data = new Vector();
		String[] ss = new String[9];
		try {
			con = DriverManager.getConnection(source);
			String str = "";
			if (cusid == null || cusid.equalsIgnoreCase("ALL"))
				str = "select * from Customers where RestaurantID=?";
			else
				str = "select * from Customers where (RestaurantID=? and customerid=?)";
			ResultSet rs = null;
			PreparedStatement ste = con.prepareStatement(str);
			ste.setString(1, id);
			if (cusid != null && !cusid.equalsIgnoreCase("ALL"))
				ste.setString(2, cusid);

			rs = ste.executeQuery();
			// 0-first,1=last,2=street,3=apt,4=city,5=state,6=zip,7=phone
			while (rs.next()) {

				ss = new String[9];
				ss[0] = (rs.getString("customerid")).trim();
				ss[1] = (rs.getString("FirstName")).trim();
				ss[2] = (rs.getString("LastName")).trim();
				ss[3] = (rs.getString("Street")).trim();
				ss[4] = (rs.getString("Apartment")).trim();
				ss[5] = (rs.getString("City")).trim();
				ss[6] = (rs.getString("State")).trim();
				ss[7] = (rs.getString("Zip")).trim();
				ss[8] = (rs.getString("Phone")).trim();
				data.add(ss);

			}
			rs.close();
			ste.close();
			con.close();
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;
	}

	public Vector getSelection(java.sql.Date ndate) {

		String[] ss = new String[15];
		Vector data = new Vector();
		try {
			con = DriverManager.getConnection(source);

			ResultSet rs = null;
			String str = "select Results.code,totalbi,Moneybi,shoubi,bigbuy,bigsell,smallbuy,smallsell,fanshourate,"
					+ "GuaBuy,GuaSell,caibuy,caisell,virtualPrice,cname,status from Results,CodeName where (Results.code=CodeName.code and Results.TDate=?)";
				PreparedStatement ste = con.prepareStatement(str);
			ste.setDate(1, ndate);
			boolean zisu = false;
			
			rs = ste.executeQuery();

			
			double money = 0, totalbi = 0, shoubi = 0, bigb = 0, bigs = 0, smallb, smalls, fsr, caibuy, caisell, vp = 0;
			while (rs.next()) {
				ss = new String[16];
				// {"Code","DifDS","DifDAR","CR","CB","CS","CBR","CSR","Name","DifBD","DifSD","DifBS","DifSS","TBI","SBI"}
				ss[0] = rs.getString("Code");
				zisu = rs.getBoolean("status");
				if (zisu)
					continue;
				totalbi = rs.getDouble("TotalBi");
				money = rs.getDouble("MoneyBi");
				shoubi = rs.getDouble("ShouBi");
				bigb = rs.getDouble("BigBuy");
				bigs = rs.getDouble("BigSell");
				smallb = rs.getDouble("SmallBuy");
				smalls = rs.getDouble("SmallSell");
				fsr = rs.getDouble("FanShouRate");
				caibuy = rs.getDouble("CaiBuy");
				caisell = rs.getDouble("CaiSell");
				vp = rs.getDouble("VirtualPrice");
				ss[1] = ""
						+ (long) (money / shoubi * (bigb - bigs - smallb + smalls));
				ss[2] = ""
						+ (int) (100 * (bigb - bigs - smallb + smalls) / (bigb
								+ bigs + smallb + smalls));
				ss[3] = "" + (int) (10000 * fsr);
				ss[4] = "" + (long) (money / shoubi * caibuy);
				// *
				ss[5] = "" + (long) (money / shoubi * caisell);
				ss[6] = "" + (int) (100 * caibuy / (totalbi * shoubi));
				ss[7] = "" + (int) (100 * caisell / (totalbi * shoubi));
				ss[8] = rs.getString("CName");
				ss[9] = "" + (int) bigb;
				ss[10] = "" + (int) bigs;
				ss[11] = "" + (int) smallb;
				ss[12] = "" + (int) smalls;
				ss[13] = "" + (int) totalbi;
				ss[14] = "" + (int) shoubi;
				ss[15] = "" + (float) (vp);
				data.add(ss);
			}

			rs.close();
			ste.close();
			con.close();
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;
	}

	public Vector getStockIDS() {
		Vector data = new Vector();

		if (true)
			return data;

		String[] ss = null;
		try {
			con = DriverManager.getConnection(source);
			Statement ste = con.createStatement();
			ResultSet rs = null;
			String str = null;

			str = "select * from CodeName ";
			Object o = null;

			rs = ste.executeQuery(str);
			// 0-id,1=title,2=version,3=location,4=catagory,5-publisher,6=summary,7=createDay,8=status,9=account
			while (rs.next()) {
				ss = new String[3];
				ss[0] = (rs.getString("Code")).trim();
				ss[1] = (rs.getString("CName")).trim();
				ss[2] = (rs.getString("ShiChang")).trim();
				data.add(ss);

			}

			rs.close();
			ste.close();
			con.close();
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;
	}

	public Vector getMenu(String resID, String catagory) {
		Vector data = new Vector();
		String[] ss = null;
		try {
			con = DriverManager.getConnection(source);
			String str = null;
			if (catagory == null || catagory.equalsIgnoreCase("ALL"))
				str = "select * from menu where restaurantID=?";
			else
				str = "select * from menu where (restaurantID=? and catagory=?)";
			PreparedStatement ste = con.prepareStatement(str);

			ResultSet rs = null;
			ste.setString(1, resID);
			if (catagory != null && !catagory.equalsIgnoreCase("ALL"))
				ste.setString(2, catagory);
			rs = ste.executeQuery();
			// 0-first,1=last,2=street,3=apt,4=city,5=state,6=zip,7=phone
			while (rs.next()) {
				ss = new String[6];
				ss[0] = (rs.getString("ItemID")).trim();
				ss[1] = (rs.getString("Name")).trim();
				ss[2] = "" + rs.getDouble("Price");
				ss[3] = (rs.getString("Hot")).trim();
				ss[4] = rs.getString("Catagory").toString();
				ss[5] = (rs.getString("Status")).trim();
				data.add(ss);
			}

			rs.close();
			ste.close();
			con.close();
			return data;

		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}
		return data;
	}

}
