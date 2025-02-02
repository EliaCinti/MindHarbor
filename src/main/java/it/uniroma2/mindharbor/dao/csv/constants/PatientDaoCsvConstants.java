package it.uniroma2.mindharbor.dao.csv.constants;

public class PatientDaoCsvConstants {
    private PatientDaoCsvConstants() {
        /* empty constructor : no instance */
    }
    public static final String[] HEADER = {"Username", "Birth Date", "Psychologist"};
    public static final String PATH_NAME_PATIENTS = "db/csv/patient_db.csv";
    public static final int PATIENT_INDEX_USERNAME = 0;
    public static final int PATIENT_INDEX_BIRTHDATE = 1;
    public static final int PATIENT_INDEX_PSYCOLOGIST = 2;
}
