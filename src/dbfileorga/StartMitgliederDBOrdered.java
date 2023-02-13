package dbfileorga;

public class StartMitgliederDBOrdered {

	public static void main(String[] args) throws InterruptedException {
			MitgliederDB db = new MitgliederDB(true);
			System.out.println(db);

			// read the a record number e.g. 32 (119;2;44;Albers;Hans;07.10.75;01.05.89;90;25)
			Record rec = db.read(32);
			if (rec == null) {System.out.println("Mitglied mit gesuchter Position nicht vorhanden!");}
			else {System.out.println("Gesuchter Eintrag anhand Position: " + rec);}
			Thread.sleep(2000);

			//find and read a record with a given Mitgliedesnummer e.g 95 / without binary search
			rec = db.read(db.findPos("95"));
			if (rec == null) {System.out.println("Mitglied mit gesuchter Nummer nicht vorhanden!");}
			else {System.out.println("Gesuchter Eintrag anhand Mitgliedsnummer: " + rec);}
			Thread.sleep(2000);

			//modify (ID95 Steffi Brahms wird zu ID 95 Steffi Bach)
			db.modify(db.findPos("95"), new Record("95;3;13;Bach;Steffi;04.04.06;01.02.16;;5"));
			System.out.println("Geänderter DS: " + db.read(db.findPos("95")));
			Thread.sleep(2000);

			//delete the record with Mitgliedsnummer 97 (97;1;65;Krapp;Theo;10.10.87;01.03.07;115;25)
			db.delete(db.findPos("97"));
			System.out.println(db);

	}

}
