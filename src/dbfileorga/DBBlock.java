package dbfileorga;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DBBlock implements Iterable<Record> {
	public final static int BLOCKSIZE = 256;
	public final static char RECDEL = '|';
	public final static char DEFCHAR =  '\u0000';

	private char block[] = new char[BLOCKSIZE];

	/**
	 * Searches the record with number recNum in the DBBlock. 
	 * @param  recNum is the number of Record 1 = first record
	 * @return record with the specified number or null if record was not found
	 */
	public Record getRecord(int recNum){
		int currRecNum = 1; //first Record starts at 0
		for (int i = 0; i <block.length;++i){
			if (currRecNum == recNum){
				return getRecordFromBlock(i);
			}
			if (block[i] == RECDEL){
				currRecNum++;
			}
		}
		return null;
	}

	/**
	 * Searches the position of searched record in the DBBlock
	 * @param searchTerm is searched term
	 * @return the position of searched record in the DBBlock
	 */
	public int getPositionOfSearchedRecord(String searchTerm){
		int count = 0;
		for (char c : block) {
			if (c == RECDEL) {
				count++;
			}
		}

		return count;
	}

	private Record getRecordFromBlock(int startPos) {
		int endPos = getEndPosOfRecord(startPos);
		if (endPos != -1){ 
			return new Record (Arrays.copyOfRange(block, startPos, endPos));
		}else{
			return null;
		}
	}

	public int getEndPosOfRecord(int startPos){
		int currPos = startPos;
		while( currPos < block.length ){
			if (block[currPos]==RECDEL){
				return currPos;
			}else{
				currPos++;
			}
		}
		return -1;
	}

	/**
	 * Searches start position of record in DBBlock
	 * @param recNumInBlock is the searched record in DBBlock
	 * @return the start position of record with specified number or null if record was not found
	 */
	public int getStartPosOfRecord(int recNumInBlock){
		int currPos = 0;
		int currRec = 1;
		while(currPos < block.length){
			if (block[currPos]==RECDEL){
				currRec++;
			}
			if(currRec == recNumInBlock){
				return currPos;
			}
			currPos++;
		}
		return -1;
	}


	/**
	 * Returns the number of records that are in the block
	 * @return number of records stored in this DBBlock
	 */	
	public int getNumberOfRecords(){
		int count = 0;
		for (char c : block) {
			if (c == RECDEL) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Inserts a record at the end of the block
	 * @param record the record to insert
	 * @return returns the last position (the position of the RECDEL char) of the inserted record
	 * 		   returns -1 if the insert fails
	 */
	public int insertRecordAtTheEnd(Record record){
		int startPos = findEmptySpace();
		return insertRecordAtPos(startPos, record);
	}

	/**
	 * Deletes all characters from a record
	 * @param startpos start of a record
	 * @param endpos end of a record
	 */
	public void deleteRecord(int startpos, int endpos){
		/*
		if(getNumberOfRecords() == getRecordNumberInBlock(endpos+1)){
			block[endpos] = ' ';
		}
		*/

		int currPos = startpos;
		if(startpos!=0) currPos++;
		while (currPos <= endpos){
			block[currPos] = DEFCHAR;
			currPos++;
		}
	}

	/**
	 * searched record number in block
	 * @param charPosition is the start of the searched block
	 * @return number of records before the searched record
	 */
	public int getRecordNumberInBlock(int charPosition){
		int count = 0;
		for (int i = 0; i <charPosition;++i){
			if (block[i] == RECDEL){
				count++;
			}
		}
		return count;
	}

	/**
	 * checks if enough space is in the block to insert record
	 * @param length is number of characters of a record
	 * @return
	 */
	public int findSpace(int length) {
		int currPos = 0;
		int space = 0;
		while(currPos < block.length){
			if (block[currPos]==' ' || block[currPos] == DEFCHAR){
				space++;

				if(space >= length){
					return currPos-space+1;
				}
				currPos++;
				continue;
			}
			if (block[currPos]!=' '){
				space=0;
			}
			currPos++;
		}

		return -1;
	}
	
	/**
	 * deletes the content of the block
	 */
	public void delete(){
		block = new char[BLOCKSIZE];
	}

	/**
	 * Inserts a record beginning at position startPos
	 * @param startPos the postition to start inserting the record
	 * @param record the record to insert
	 * @return returns the last position (the position of the RECDEL char) of the inserted record
	 * 		   returns -1 if the insert fails
	 */	
	public int insertRecordAtPos(int startPos, Record record) {
		//we need to insert the record plus the RECDEL
		int n = record.length();
		if (startPos+n+1 > block.length){
			return -1; // record does not fit into the block;
		}
		for (int i = 0; i < n; ++i) {
			block[i+startPos] = record.charAt(i);
		}
		block[n+startPos]= RECDEL;
		return n+startPos;
	}

	public void cleanUpBlock(){
		int lastcharacter = 0;
		for (int i = 0; i<block.length; i++){
			if (block[i] != DEFCHAR){
				block[lastcharacter] = block[i];
				lastcharacter ++;
			}
		}

	}

	public int countEmptySpaceInBlock(){
		int counter = 0;
		for (int i = 0; i <block.length;++i){
			if (block[i] == DEFCHAR){
				counter++;
			}
		}
		return counter;
	}

	private int findEmptySpace(){
		for (int i = 0; i <block.length;++i){
			if (block[i] == DEFCHAR){
				return i;
			}
		}
		return block.length;		
	}
	
	@Override
	public String toString(){
		StringBuilder result = new StringBuilder();
		for (char c : block) {

			if (c == DEFCHAR) {
				continue;
			}
			if (c == RECDEL) {
				result.append("\n");
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}



	@Override
	public Iterator<Record> iterator() {
		return new BlockIterator();
	}
	
	
	private class BlockIterator implements Iterator<Record> {
		private int currRec;

		public  BlockIterator() {
			this.currRec = 0;
		}

		public boolean hasNext() {
			return getRecord(currRec + 1) != null;
		}

		public Record next() {
			Record result = getRecord(++currRec);
			if (result == null){
				throw new NoSuchElementException();
			}else{
				return result;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


}
