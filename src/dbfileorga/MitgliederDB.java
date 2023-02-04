package dbfileorga;

import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MitgliederDB implements Iterable<Record>
{
	
	protected DBBlock db[] = new DBBlock[8];
	
	
	public MitgliederDB(boolean ordered){
		this();
		insertMitgliederIntoDB(ordered);
		
	}
	public MitgliederDB(){
		initDB();
	}
	
	private void initDB() {
		for (int i = 0; i<db.length; ++i){
			db[i]= new DBBlock();
		}
		
	}
	private void insertMitgliederIntoDB(boolean ordered) {
		MitgliederTableAsArray mitglieder = new MitgliederTableAsArray();
		String mitgliederDatasets[];
		if (ordered){
			mitgliederDatasets = mitglieder.recordsOrdered;
		}else{
			mitgliederDatasets = mitglieder.records;
		}
		for (String currRecord : mitgliederDatasets ){
			appendRecord(new Record(currRecord));
		}	
	}

		
	protected int appendRecord(Record record){
		//search for block where the record should be appended
		int currBlock = getBlockNumOfRecord(getNumberOfRecords());
		int result = db[currBlock].insertRecordAtTheEnd(record);
		if (result != -1 ){ //insert was successful
			return result;
		}else if (currBlock < db.length) { // overflow => insert the record into the next block
			return db[currBlock+1].insertRecordAtTheEnd(record);
		}
		return -1;
	}
	

	@Override
	public String toString(){
		String result = new String();
		for (int i = 0; i< db.length ; ++i){
			result += "Block "+i+"\n";
			result += db[i].toString();
			result += "-------------------------------------------------------------------------------------\n";
		}
		return result;
	}
	
	/**
	 * Returns the number of Records in the Database
	 * @return number of records stored in the database
	 */
	public int getNumberOfRecords(){
		int result = 0;
		for (DBBlock currBlock: db){
			result += currBlock.getNumberOfRecords();
		}
		return result;
	}

	/**
	 * Returns the number of Records till a specific block
	 * @return number of records stored in the blocks before
	 */
	public int getNumberOfRecordsTillBlock(int blocknumber){
		int result = 0;
		int currentBlock = 0;
		for (DBBlock currBlock: db){
			result += currBlock.getNumberOfRecords();
			if (currentBlock == blocknumber) return result;
			currentBlock ++;
		}
		return result;
	}
	
	/**
	 * Returns the block number of the given record number 
	 * @param recNum the record number to search for
	 * @return the block number or -1 if record is not found
	 */
	public int getBlockNumOfRecord(int recNum){
		int recCounter = 0;
		for (int i = 0; i< db.length; ++i){
			if (recNum <= (recCounter+db[i].getNumberOfRecords())){
				return i ;
			}else{
				recCounter += db[i].getNumberOfRecords();
			}
		}
		return -1;
	}
		
	public DBBlock getBlock(int i){
		return db[i];
	}
	
	
	/**
	 * Returns the record matching the record number
	 * @param recNum the term to search for
	 * @return the record matching the search term
	 */
	public Record read(int recNum){
		int blocknumber = getBlockNumOfRecord(recNum);
		int recordsInBlocksBefore = recordsInBlockBefore(blocknumber);
		DBBlock searchedBlock = getBlock(blocknumber);
		return searchedBlock.getRecord(recNum-recordsInBlocksBefore);
	}

	public int recordsInBlockBefore (int blocknumber){
		int recordsInBlocksBefore = 0;
		if (blocknumber != 0) {
			recordsInBlocksBefore = getNumberOfRecordsTillBlock(blocknumber - 1);
		}
		return recordsInBlocksBefore;
	}
	
	/**
	 * Returns the number of the first record that matches the search term
	 * @param searchTerm the term to search for
	 * @return the number of the record in the DB -1 if not found
	 */
	public int findPos(String searchTerm){
		int numberOfRecords = getNumberOfRecords();
		Record record;
		for (int i = 1; i <= numberOfRecords; i++){
			record = read(i);
			if (record.getAttribute(1).equals(searchTerm)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Inserts the record into the file and returns the record number
	 * @param record
	 * @return the record number of the inserted record
	 */
	public int insert(Record record){
		int length = record.length();
		for (DBBlock currBlock: db){
			int position = currBlock.findSpace(length);
			if (position >= 0) {
				currBlock.insertRecordAtPos(position, record);
				return findPos(record.getAttribute(1));

			}
		}
		appendRecord(record);
		return findPos(record.getAttribute(1));
	}
	
	/**
	 * Deletes the record specified 
	 * @param numRecord number of the record to be deleted
	 */
	public void delete(int numRecord){

		int blockNum = getBlockNumOfRecord(numRecord);
		int recordsInBlocksBefore = recordsInBlockBefore(blockNum);

		int startPos = this.db[blockNum].getStartPosOfRecord(numRecord-recordsInBlocksBefore);
		int endPos = this.db[blockNum].getEndPosOfRecord(startPos+1);

		this.db[blockNum].deleteRecord(startPos,endPos);

	}
	
	/**
	 * Replaces the record at the specified position with the given one.
	 * @param numRecord the position of the old record in the db
	 * @param record the new record
	 * 
	 */
	public void modify(int numRecord, Record record){
		//TODO
	}

	
	@Override
	public Iterator<Record> iterator() {
		return new DBIterator();
	}
 
	private class DBIterator implements Iterator<Record> {

		    private int currBlock = 0;
		    private Iterator<Record> currBlockIter= db[currBlock].iterator();
	 
	        public boolean hasNext() {
	            if (currBlockIter.hasNext()){
	                return true; 
	            } else if (currBlock < db.length) { //continue search in the next block
	                return db[currBlock+1].iterator().hasNext();
	            }else{ 
	                return false;
	            }
	        }
	 
	        public Record next() {	        	
	        	if (currBlockIter.hasNext()){
	        		return currBlockIter.next();
	        	}else if (currBlock < db.length){ //continue search in the next block
	        		currBlockIter= db[++currBlock].iterator();
	        		return currBlockIter.next();
	        	}else{
	        		throw new NoSuchElementException();
	        	}
	        }
	 
	        @Override
	        public void remove() {
	        	throw new UnsupportedOperationException();
	        }
	    } 
	 

}
