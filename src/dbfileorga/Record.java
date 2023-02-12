package dbfileorga;

import java.util.Arrays;

public class Record {
	
	public final static char ATTDEL = ';';

	private char [] record;
	
	public Record (char [] record){
		this.record = record;
		
	}
	
	public Record (String record){
		 this(record.toCharArray());
	}
	
	public int length(){
		return record.length;
	}
	
	public char charAt(int i){
		return record[i];
	}
		
	public String toString(){
		return 	new String (record);
	}

	/**
	 * deletes every character of a record on the position
	 * @param pos position of the record to be deleted
	 * @return returns 1 if the deleting was successful or -1 if it was not successful
	 */
	public int deleteCharAt(int pos){
		if (record[pos] != '-'){
			record[pos] = '-';
			return 1;
		}
		return -1;
	}
	
	/**
	 * Get the attribute with the attrNum number
	 * @param attrNum the number of the attribute to be returned
	 * @return the value of the Attribute as String
	 */
	public String getAttribute(int attrNum){
		int currAttributeNum = 1;
		for (int i = 0; i < record.length; ++i){
			if (currAttributeNum == attrNum){
				int end = getEndPosOfAttribute(i);
				return new String(Arrays.copyOfRange(record, i, end));
			}
			if (record[i]==ATTDEL){
				currAttributeNum++;
			}
		}
		return null; 
	}
	
	
	private int getEndPosOfAttribute(int startPos) {
		int currPos = startPos;
		while( currPos <record.length ){
			if (record[currPos]==ATTDEL){
				return currPos;
			}else{
				currPos++;
			}
		}
		return currPos;
	}
	
	/**
	 * Returns the number of attributes of the record
	 * @return the number of attributes of the record
	 */
	public int getNumOfAttributes(){
		int count = 1;
		for (int i = 0; i < record.length; ++i){
			if (record[i]==ATTDEL){
				count++;
			}
		}
		return count;
	}

		
}
