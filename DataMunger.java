import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/*There are total 5 DataMungertest files:
 *
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 *
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 */

 /*
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 *
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 */

/*
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 */

 /* 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions() *
 * Once you implement the above 2 methods,run DataMungerTestTask4.java *
 * Once you implement all the methods run DataMungerTest.java.
 * This test case consist of all
 * the test cases together.
 */

public class DataMunger {


	/*
	 * Default constructor
	 */
	public DataMunger() {

	}

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(final String queryString) {

		final String[] queryStr = queryString.split(" ");
		String[] output = new String[queryStr.length];
		for (int i = 0; i < queryStr.length; i++) {
			final String val = queryStr[i];
			output[i] =val.toLowerCase();
		}

		return output;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 *
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(final String queryString) {

		final String[] queryStr = queryString.split(" ");

			for (int i = 0; i < queryStr.length; i++) {
				if(queryStr[i].equals("from")) {
					return queryStr[i+1];
				}
			}
		return null;
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 *
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */

	public String getBaseQuery(final String queryString) {

		final String[] queryStr = queryString.split(" ");

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].equals("from")) {
				return builder.append(queryStr[i]).append(" ").append(queryStr[i+1]).toString();
			} else {
				builder.append(queryStr[i]).append(" ");
			}
		}
		return builder.toString();
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 *
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 *
	 */

	public String[] getFields(final String queryString) {

		final String[] queryStr = queryString.split(" ");
		String[] fieldArr = null;

		for (String string : queryStr) {

			if(string.contains(",")) {
				fieldArr = string.split(",");
			}
		}

		if(fieldArr == null){
			fieldArr = new String[] {"*"};
		}
		return fieldArr;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String getConditionsPartQuery(final String queryString) {

		final String[] queryStr = queryString.split(" ");

		StringBuilder builder = new StringBuilder();
		String output = null;
		boolean flag = false;

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].equals("where")) {
				flag = true;
			} else if(queryStr[i].equals("group") || queryStr[i].equals("order")) {
				flag = false;
			}
			else {
				if(flag){
					builder.append(queryStr[i]);
					builder.append(" ");
				}
			}
		}
		if(!builder.toString().isEmpty()){
			output = builder.toString().toLowerCase().trim();
		}
		return output;
	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 *
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 *
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(final String queryString) {

		String[] output = null;

		final String[] queryStr = queryString.split(" ");

		StringBuilder builder = new StringBuilder();
		boolean flag = false;

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].equals("where")) {
				flag = true;
			} else {
				if(flag){
					builder.append(queryStr[i]).append(" ");
				}
			}
		}

		String val = builder.toString().trim();

		String[] cond = getCondOutput(val);


		if(cond!= null && cond.length>0){
			output = new String[cond.length];

			for (int i = 0; i < cond.length; i++) {
				output[i] = cond[i].trim().toLowerCase();
			}
		}


		return output;
	}

	private String[] getCondOutput(String val) {

		String[] cond = null;
		if(!val.isEmpty()){
			String[] condPartArr = val.split(" ");
			StringBuilder conditions = new StringBuilder();
			for (int i = 0; i < condPartArr.length; i++) {
				if(condPartArr[i].equalsIgnoreCase("and")
						|| condPartArr[i].equalsIgnoreCase("or")){
					conditions.append(",");
				}else if(condPartArr[i].equalsIgnoreCase("order")
							|| condPartArr[i].equalsIgnoreCase("group")){
					break;
				} else {
					conditions.append(" ");
					conditions.append(condPartArr[i]);
				}
			}
			cond =  conditions.toString().trim().split(",");
		}

			return cond;
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 *
	 */

	public String[] getLogicalOperators(final String queryString) {

		final String[] queryStr = queryString.split(" ");
		boolean flag = false;
		List<String> list = new ArrayList<String>();
		String[] outPut = null;

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].equals("where")) {
				flag = true;
			} else {
				if(flag && (queryStr[i].equalsIgnoreCase("and")
						|| queryStr[i].equalsIgnoreCase("or"))){
					list.add(queryStr[i]);
				}
			}
		}

		if(list.size()>0){
			outPut = new String[list.size()];

			outPut = list.toArray(outPut);

		}


		return outPut;
	}

	/*
	 * This method extracts the order by fields from the query string. Note:
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(final String queryString) {

		final String[] queryStr = queryString.split(" ");
		boolean flag = false;
		List<String> list = new ArrayList<String>();
		String[] outPut = null;

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].equals("by")) {
				flag = true;
			} else {
				if(flag){
					list.add(queryStr[i]);
				}
			}
		}

		if(list.size()>0){
			outPut = new String[list.size()];

			outPut = list.toArray(outPut);

		}

		return outPut;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 *
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(final String queryString) {

		final String[] queryStr = queryString.split(" ");
		boolean flag = false;
		List<String> list = new ArrayList<String>();
		String[] outPut = null;

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].equals("by")) {
				if(queryStr[i-1].equals("group")){
					flag = true;
				}
				continue;
			} else {
				if(flag){
					list.add(queryStr[i]);
				}
			}
		}

		if(!list.isEmpty()){
			outPut = new String[list.size()];

			outPut = list.toArray(outPut);
		}

		return outPut;
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 *
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(final String queryString) {

		final String[] queryStr = queryString.split(" ");
		List<String> list = new ArrayList<String>();
		String[] outPut = null;
		List<String> finallist = new ArrayList<String>();

		for (int i = 0; i < queryStr.length; i++) {
			if(queryStr[i].contains("(")) {
				list.add(queryStr[i]);
			}
		}

		if(!list.isEmpty()){
			for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				String[] arr = string.split(",");

				for (int i = 0; i < arr.length; i++) {
					String val = arr[i];
					if(val.contains("(")){
							finallist.add(val.trim());
					}
				}
			}

			if(!finallist.isEmpty()){
				outPut = new String[finallist.size()];
				outPut = finallist.toArray(outPut);
			}

		}
		return outPut;
	}
}