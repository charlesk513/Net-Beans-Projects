package charlesk.com.hospital_management_system;

import java.util.Locale;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

/**
 * Central dispatcher.
 * Selects the correct module service using the request sender.
 */
public class HospitalService {

    private final PatientsService patientsService = new PatientsService();
    private final LoginService loginService = new LoginService();

    private final AdmissionsService admissionsService = new AdmissionsService(this);
    private final AppointmentsService appointmentsService = new AppointmentsService(this);
    private final BillsService billsService = new BillsService(this);
    private final DepartmentsService departmentsService = new DepartmentsService(this);
    private final DiagnosesService diagnosesService = new DiagnosesService(this);
    private final DoctorsService doctorsService = new DoctorsService(this);
    private final LabTestsService labTestsService = new LabTestsService(this);
    private final MedicinesService medicinesService = new MedicinesService(this);
    private final NursesService nursesService = new NursesService(this);
    private final PaymentsService paymentsService = new PaymentsService(this);
    private final PrescriptionItemsService prescriptionItemsService = new PrescriptionItemsService(this);
    private final PrescriptionsService prescriptionsService = new PrescriptionsService(this);
    private final RoomsService roomsService = new RoomsService(this);
    private final WardsService wardsService = new WardsService(this);

    /**
     * This method is called by RequestRouter.
     * The sourceForm determines which module receives the request.
     */
    public Object execute(String sourceForm, String entity,
                          String operation, DataDTO dto) throws Exception {

        if (sourceForm == null || entity == null || operation == null) {
            throw new IllegalArgumentException(
                    "Source form, entity and operation are required.");
        }

        String sender = sourceForm.trim().toUpperCase(Locale.ROOT);
        String requestedEntity = entity.trim().toUpperCase(Locale.ROOT);

        if (dto == null) {
            dto = new DataDTO();
        }

        switch (sender) {
            case "PATIENT_GUI":
                checkEntity(requestedEntity, "PATIENTS");
                return patientsService.execute(operation.toUpperCase(Locale.ROOT), dto);

            case "LOGIN_GUI":
                checkEntity(requestedEntity, "LOGIN");
                return loginService.execute(operation.toUpperCase(Locale.ROOT), dto);

            case "ADMISSIONS_GUI":
                return admissionsService.execute(requestedEntity, operation, dto);

            case "APPOINTMENTS_GUI":
                return appointmentsService.execute(requestedEntity, operation, dto);

            case "BILLS_GUI":
                return billsService.execute(requestedEntity, operation, dto);

            case "DEPARTMENTS_GUI":
                return departmentsService.execute(requestedEntity, operation, dto);

            case "DIAGNOSES_GUI":
                return diagnosesService.execute(requestedEntity, operation, dto);

            case "DOCTORS_GUI":
                return doctorsService.execute(requestedEntity, operation, dto);

            case "LAB_TESTS_GUI":
                return labTestsService.execute(requestedEntity, operation, dto);

            case "MEDICINES_GUI":
                return medicinesService.execute(requestedEntity, operation, dto);

            case "NURSES_GUI":
                return nursesService.execute(requestedEntity, operation, dto);

            case "PAYMENTS_GUI":
                return paymentsService.execute(requestedEntity, operation, dto);

            case "PRESCRIPTION_ITEMS_GUI":
                return prescriptionItemsService.execute(requestedEntity, operation, dto);

            case "PRESCRIPTIONS_GUI":
                return prescriptionsService.execute(requestedEntity, operation, dto);

            case "ROOMS_GUI":
                return roomsService.execute(requestedEntity, operation, dto);

            case "WARDS_GUI":
                return wardsService.execute(requestedEntity, operation, dto);

            default:
                throw new IllegalArgumentException(
                        "Unknown request sender: " + sender);
        }
    }

    private void checkEntity(String actualEntity, String expectedEntity) {
        if (!actualEntity.equals(expectedEntity)) {
            throw new IllegalArgumentException(
                    "Sender does not match entity. Expected "
                    + expectedEntity + " but received " + actualEntity);
        }
    }

    /**
     * Existing entity/operation dispatcher.
     * Keep your current switch statement here, unchanged,
     * except remove the LOGIN and PATIENTS cases because
     * those modules now use their own service classes.
     */

    /** Routes an entity + operation pair to the matching service method. */
    public Object executeEntity(String entity, String operation, DataDTO dto) throws Exception {
        if (entity == null || operation == null) {
            throw new IllegalArgumentException("Entity and operation are required.");
        }
        if (dto == null) dto = new DataDTO();

        String e = entity.trim().toUpperCase(Locale.ROOT);
        String op = operation.trim().toUpperCase(Locale.ROOT);

        switch (e) {
            case "ADMISSIONS":
                switch (op) {
                    case "FIRST": return firstAdmissions(dto);
                    case "LAST": return lastAdmissions(dto);
                    case "PREVIOUS": return previousAdmissions(dto);
                    case "SAVE": return saveAdmissions(dto);
                    case "FIND": return findAdmissions(dto);
                    case "DELETE": return deleteAdmissions(dto);
                    case "UPDATE": return updateAdmissions(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for ADMISSIONS: " + op);
                }
            case "APPOINTMENTS":
                switch (op) {
                    case "FIRST": return firstAppointments(dto);
                    case "LAST": return lastAppointments(dto);
                    case "PREVIOUS": return previousAppointments(dto);
                    case "SAVE": return saveAppointments(dto);
                    case "FIND": return findAppointments(dto);
                    case "DELETE": return deleteAppointments(dto);
                    case "UPDATE": return updateAppointments(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for APPOINTMENTS: " + op);
                }
            case "BILLS":
                switch (op) {
                    case "FIRST": return firstBills(dto);
                    case "LAST": return lastBills(dto);
                    case "PREVIOUS": return previousBills(dto);
                    case "SAVE": return saveBills(dto);
                    case "FIND": return findBills(dto);
                    case "DELETE": return deleteBills(dto);
                    case "UPDATE": return updateBills(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for BILLS: " + op);
                }
            case "DEPARTMENTS":
                switch (op) {
                    case "FIRST": return firstDepartments(dto);
                    case "LAST": return lastDepartments(dto);
                    case "PREVIOUS": return previousDepartments(dto);
                    case "SAVE": return saveDepartments(dto);
                    case "FIND": return findDepartments(dto);
                    case "DELETE": return deleteDepartments(dto);
                    case "UPDATE": return updateDepartments(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for DEPARTMENTS: " + op);
                }
            case "DIAGNOSES":
                switch (op) {
                    case "FIRST": return firstDiagnoses(dto);
                    case "LAST": return lastDiagnoses(dto);
                    case "PREVIOUS": return previousDiagnoses(dto);
                    case "SAVE": return saveDiagnoses(dto);
                    case "FIND": return findDiagnoses(dto);
                    case "DELETE": return deleteDiagnoses(dto);
                    case "UPDATE": return updateDiagnoses(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for DIAGNOSES: " + op);
                }
            case "DOCTORS":
                switch (op) {
                    case "FIRST": return firstDoctors(dto);
                    case "LAST": return lastDoctors(dto);
                    case "PREVIOUS": return previousDoctors(dto);
                    case "SAVE": return saveDoctors(dto);
                    case "FIND": return findDoctors(dto);
                    case "DELETE": return deleteDoctors(dto);
                    case "UPDATE": return updateDoctors(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for DOCTORS: " + op);
                }
            case "LAB_TESTS":
                switch (op) {
                    case "FIRST": return firstLabTests(dto);
                    case "LAST": return lastLabTests(dto);
                    case "PREVIOUS": return previousLabTests(dto);
                    case "SAVE": return saveLabTests(dto);
                    case "FIND": return findLabTests(dto);
                    case "DELETE": return deleteLabTests(dto);
                    case "UPDATE": return updateLabTests(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for LAB_TESTS: " + op);
                }
            
            case "MEDICINES":
                switch (op) {
                    case "FIRST": return firstMedicines(dto);
                    case "LAST": return lastMedicines(dto);
                    case "PREVIOUS": return previousMedicines(dto);
                    case "SAVE": return saveMedicines(dto);
                    case "FIND": return findMedicines(dto);
                    case "DELETE": return deleteMedicines(dto);
                    case "UPDATE": return updateMedicines(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for MEDICINES: " + op);
                }
            case "NURSES":
                switch (op) {
                    case "FIRST": return firstNurses(dto);
                    case "LAST": return lastNurses(dto);
                    case "PREVIOUS": return previousNurses(dto);
                    case "SAVE": return saveNurses(dto);
                    case "FIND": return findNurses(dto);
                    case "DELETE": return deleteNurses(dto);
                    case "UPDATE": return updateNurses(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for NURSES: " + op);
                }
                
            case "PAYMENTS":
                switch (op) {
                    case "FIRST": return firstPayments(dto);
                    case "LAST": return lastPayments(dto);
                    case "PREVIOUS": return previousPayments(dto);
                    case "SAVE": return savePayments(dto);
                    case "FIND": return findPayments(dto);
                    case "DELETE": return deletePayments(dto);
                    case "UPDATE": return updatePayments(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for PAYMENTS: " + op);
                }
            case "PRESCRIPTION_ITEMS":
                switch (op) {
                    case "FIRST": return firstPrescriptionItems(dto);
                    case "LAST": return lastPrescriptionItems(dto);
                    case "PREVIOUS": return previousPrescriptionItems(dto);
                    case "SAVE": return savePrescriptionItems(dto);
                    case "FIND": return findPrescriptionItems(dto);
                    case "DELETE": return deletePrescriptionItems(dto);
                    case "UPDATE": return updatePrescriptionItems(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for PRESCRIPTION_ITEMS: " + op);
                }
            case "PRESCRIPTIONS":
                switch (op) {
                    case "FIRST": return firstPrescriptions(dto);
                    case "LAST": return lastPrescriptions(dto);
                    case "PREVIOUS": return previousPrescriptions(dto);
                    case "SAVE": return savePrescriptions(dto);
                    case "FIND": return findPrescriptions(dto);
                    case "DELETE": return deletePrescriptions(dto);
                    case "UPDATE": return updatePrescriptions(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for PRESCRIPTIONS: " + op);
                }
            case "ROOMS":
                switch (op) {
                    case "FIRST": return firstRooms(dto);
                    case "LAST": return lastRooms(dto);
                    case "PREVIOUS": return previousRooms(dto);
                    case "SAVE": return saveRooms(dto);
                    case "FIND": return findRooms(dto);
                    case "DELETE": return deleteRooms(dto);
                    case "UPDATE": return updateRooms(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for ROOMS: " + op);
                }
            case "WARDS":
                switch (op) {
                    case "FIRST": return firstWards(dto);
                    case "LAST": return lastWards(dto);
                    case "PREVIOUS": return previousWards(dto);
                    case "SAVE": return saveWards(dto);
                    case "FIND": return findWards(dto);
                    case "DELETE": return deleteWards(dto);
                    case "UPDATE": return updateWards(dto);
                    default: throw new IllegalArgumentException("Unsupported operation for WARDS: " + op);
                }
            default: throw new IllegalArgumentException("Unknown entity: " + e);
        }
    }

    // Admissions - FIRST
    // TODO: Add the SQL/business logic for FIRST on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions FIRST SQL is not implemented yet.");
    }

    // Admissions - LAST
    // TODO: Add the SQL/business logic for LAST on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions LAST SQL is not implemented yet.");
    }

    // Admissions - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions PREVIOUS SQL is not implemented yet.");
    }

    // Admissions - SAVE
    // TODO: Add the SQL/business logic for SAVE on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions SAVE SQL is not implemented yet.");
    }

    // Admissions - FIND
    // TODO: Add the SQL/business logic for FIND on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions FIND SQL is not implemented yet.");
    }

    // Admissions - DELETE
    // TODO: Add the SQL/business logic for DELETE on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions DELETE SQL is not implemented yet.");
    }

    // Admissions - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Admissions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateAdmissions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Admissions UPDATE SQL is not implemented yet.");
    }

    // Appointments - FIRST
    // TODO: Add the SQL/business logic for FIRST on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments FIRST SQL is not implemented yet.");
    }

    // Appointments - LAST
    // TODO: Add the SQL/business logic for LAST on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments LAST SQL is not implemented yet.");
    }

    // Appointments - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments PREVIOUS SQL is not implemented yet.");
    }

    // Appointments - SAVE
    // TODO: Add the SQL/business logic for SAVE on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments SAVE SQL is not implemented yet.");
    }

    // Appointments - FIND
    // TODO: Add the SQL/business logic for FIND on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments FIND SQL is not implemented yet.");
    }

    // Appointments - DELETE
    // TODO: Add the SQL/business logic for DELETE on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments DELETE SQL is not implemented yet.");
    }

    // Appointments - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Appointments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateAppointments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Appointments UPDATE SQL is not implemented yet.");
    }

    // Bills - FIRST
    // TODO: Add the SQL/business logic for FIRST on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills FIRST SQL is not implemented yet.");
    }

    // Bills - LAST
    // TODO: Add the SQL/business logic for LAST on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills LAST SQL is not implemented yet.");
    }

    // Bills - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills PREVIOUS SQL is not implemented yet.");
    }

    // Bills - SAVE
    // TODO: Add the SQL/business logic for SAVE on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills SAVE SQL is not implemented yet.");
    }

    // Bills - FIND
    // TODO: Add the SQL/business logic for FIND on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills FIND SQL is not implemented yet.");
    }

    // Bills - DELETE
    // TODO: Add the SQL/business logic for DELETE on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills DELETE SQL is not implemented yet.");
    }

    // Bills - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Bills.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateBills(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Bills UPDATE SQL is not implemented yet.");
    }

    // Departments - FIRST
    // TODO: Add the SQL/business logic for FIRST on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments FIRST SQL is not implemented yet.");
    }

    // Departments - LAST
    // TODO: Add the SQL/business logic for LAST on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments LAST SQL is not implemented yet.");
    }

    // Departments - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments PREVIOUS SQL is not implemented yet.");
    }

    // Departments - SAVE
    // TODO: Add the SQL/business logic for SAVE on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments SAVE SQL is not implemented yet.");
    }

    // Departments - FIND
    // TODO: Add the SQL/business logic for FIND on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments FIND SQL is not implemented yet.");
    }

    // Departments - DELETE
    // TODO: Add the SQL/business logic for DELETE on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments DELETE SQL is not implemented yet.");
    }

    // Departments - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Departments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateDepartments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Departments UPDATE SQL is not implemented yet.");
    }

    // Diagnoses - FIRST
    // TODO: Add the SQL/business logic for FIRST on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses FIRST SQL is not implemented yet.");
    }

    // Diagnoses - LAST
    // TODO: Add the SQL/business logic for LAST on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses LAST SQL is not implemented yet.");
    }

    // Diagnoses - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses PREVIOUS SQL is not implemented yet.");
    }

    // Diagnoses - SAVE
    // TODO: Add the SQL/business logic for SAVE on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses SAVE SQL is not implemented yet.");
    }

    // Diagnoses - FIND
    // TODO: Add the SQL/business logic for FIND on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses FIND SQL is not implemented yet.");
    }

    // Diagnoses - DELETE
    // TODO: Add the SQL/business logic for DELETE on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses DELETE SQL is not implemented yet.");
    }

    // Diagnoses - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Diagnoses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateDiagnoses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Diagnoses UPDATE SQL is not implemented yet.");
    }

    // Doctors - FIRST
    // TODO: Add the SQL/business logic for FIRST on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors FIRST SQL is not implemented yet.");
    }

    // Doctors - LAST
    // TODO: Add the SQL/business logic for LAST on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors LAST SQL is not implemented yet.");
    }

    // Doctors - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors PREVIOUS SQL is not implemented yet.");
    }

    // Doctors - SAVE
    // TODO: Add the SQL/business logic for SAVE on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors SAVE SQL is not implemented yet.");
    }

    // Doctors - FIND
    // TODO: Add the SQL/business logic for FIND on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors FIND SQL is not implemented yet.");
    }

    // Doctors - DELETE
    // TODO: Add the SQL/business logic for DELETE on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors DELETE SQL is not implemented yet.");
    }

    // Doctors - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Doctors.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateDoctors(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Doctors UPDATE SQL is not implemented yet.");
    }

    // Lab Tests - FIRST
    // TODO: Add the SQL/business logic for FIRST on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests FIRST SQL is not implemented yet.");
    }

    // Lab Tests - LAST
    // TODO: Add the SQL/business logic for LAST on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests LAST SQL is not implemented yet.");
    }

    // Lab Tests - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests PREVIOUS SQL is not implemented yet.");
    }

    // Lab Tests - SAVE
    // TODO: Add the SQL/business logic for SAVE on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests SAVE SQL is not implemented yet.");
    }

    // Lab Tests - FIND
    // TODO: Add the SQL/business logic for FIND on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests FIND SQL is not implemented yet.");
    }

    // Lab Tests - DELETE
    // TODO: Add the SQL/business logic for DELETE on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests DELETE SQL is not implemented yet.");
    }

    // Lab Tests - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Lab Tests.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateLabTests(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Lab Tests UPDATE SQL is not implemented yet.");
    }

    // Medicines - FIRST
    // TODO: Add the SQL/business logic for FIRST on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines FIRST SQL is not implemented yet.");
    }

    // Medicines - LAST
    // TODO: Add the SQL/business logic for LAST on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines LAST SQL is not implemented yet.");
    }

    // Medicines - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines PREVIOUS SQL is not implemented yet.");
    }

    // Medicines - SAVE
    // TODO: Add the SQL/business logic for SAVE on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines SAVE SQL is not implemented yet.");
    }

    // Medicines - FIND
    // TODO: Add the SQL/business logic for FIND on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines FIND SQL is not implemented yet.");
    }

    // Medicines - DELETE
    // TODO: Add the SQL/business logic for DELETE on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines DELETE SQL is not implemented yet.");
    }

    // Medicines - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Medicines.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateMedicines(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Medicines UPDATE SQL is not implemented yet.");
    }

    // Nurses - FIRST
    // TODO: Add the SQL/business logic for FIRST on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses FIRST SQL is not implemented yet.");
    }

    // Nurses - LAST
    // TODO: Add the SQL/business logic for LAST on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses LAST SQL is not implemented yet.");
    }

    // Nurses - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses PREVIOUS SQL is not implemented yet.");
    }

    // Nurses - SAVE
    // TODO: Add the SQL/business logic for SAVE on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses SAVE SQL is not implemented yet.");
    }

    // Nurses - FIND
    // TODO: Add the SQL/business logic for FIND on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses FIND SQL is not implemented yet.");
    }

    // Nurses - DELETE
    // TODO: Add the SQL/business logic for DELETE on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses DELETE SQL is not implemented yet.");
    }

    // Nurses - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Nurses.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateNurses(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Nurses UPDATE SQL is not implemented yet.");
    }

    // Payments - FIRST
    // TODO: Add the SQL/business logic for FIRST on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstPayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments FIRST SQL is not implemented yet.");
    }

    // Payments - LAST
    // TODO: Add the SQL/business logic for LAST on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastPayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments LAST SQL is not implemented yet.");
    }

    // Payments - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousPayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments PREVIOUS SQL is not implemented yet.");
    }

    // Payments - SAVE
    // TODO: Add the SQL/business logic for SAVE on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object savePayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments SAVE SQL is not implemented yet.");
    }

    // Payments - FIND
    // TODO: Add the SQL/business logic for FIND on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findPayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments FIND SQL is not implemented yet.");
    }

    // Payments - DELETE
    // TODO: Add the SQL/business logic for DELETE on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deletePayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments DELETE SQL is not implemented yet.");
    }

    // Payments - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Payments.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updatePayments(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Payments UPDATE SQL is not implemented yet.");
    }

    // Prescription Items - FIRST
    // TODO: Add the SQL/business logic for FIRST on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstPrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items FIRST SQL is not implemented yet.");
    }

    // Prescription Items - LAST
    // TODO: Add the SQL/business logic for LAST on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastPrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items LAST SQL is not implemented yet.");
    }

    // Prescription Items - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousPrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items PREVIOUS SQL is not implemented yet.");
    }

    // Prescription Items - SAVE
    // TODO: Add the SQL/business logic for SAVE on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object savePrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items SAVE SQL is not implemented yet.");
    }

    // Prescription Items - FIND
    // TODO: Add the SQL/business logic for FIND on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findPrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items FIND SQL is not implemented yet.");
    }

    // Prescription Items - DELETE
    // TODO: Add the SQL/business logic for DELETE on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deletePrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items DELETE SQL is not implemented yet.");
    }

    // Prescription Items - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Prescription Items.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updatePrescriptionItems(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescription Items UPDATE SQL is not implemented yet.");
    }

    // Prescriptions - FIRST
    // TODO: Add the SQL/business logic for FIRST on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstPrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions FIRST SQL is not implemented yet.");
    }

    // Prescriptions - LAST
    // TODO: Add the SQL/business logic for LAST on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastPrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions LAST SQL is not implemented yet.");
    }

    // Prescriptions - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousPrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions PREVIOUS SQL is not implemented yet.");
    }

    // Prescriptions - SAVE
    // TODO: Add the SQL/business logic for SAVE on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object savePrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions SAVE SQL is not implemented yet.");
    }

    // Prescriptions - FIND
    // TODO: Add the SQL/business logic for FIND on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findPrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions FIND SQL is not implemented yet.");
    }

    // Prescriptions - DELETE
    // TODO: Add the SQL/business logic for DELETE on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deletePrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions DELETE SQL is not implemented yet.");
    }

    // Prescriptions - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Prescriptions.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updatePrescriptions(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Prescriptions UPDATE SQL is not implemented yet.");
    }

    // Rooms - FIRST
    // TODO: Add the SQL/business logic for FIRST on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms FIRST SQL is not implemented yet.");
    }

    // Rooms - LAST
    // TODO: Add the SQL/business logic for LAST on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms LAST SQL is not implemented yet.");
    }

    // Rooms - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms PREVIOUS SQL is not implemented yet.");
    }

    // Rooms - SAVE
    // TODO: Add the SQL/business logic for SAVE on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms SAVE SQL is not implemented yet.");
    }

    // Rooms - FIND
    // TODO: Add the SQL/business logic for FIND on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms FIND SQL is not implemented yet.");
    }

    // Rooms - DELETE
    // TODO: Add the SQL/business logic for DELETE on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms DELETE SQL is not implemented yet.");
    }

    // Rooms - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Rooms.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateRooms(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Rooms UPDATE SQL is not implemented yet.");
    }

    // Wards - FIRST
    // TODO: Add the SQL/business logic for FIRST on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object firstWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards FIRST SQL is not implemented yet.");
    }

    // Wards - LAST
    // TODO: Add the SQL/business logic for LAST on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object lastWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards LAST SQL is not implemented yet.");
    }

    // Wards - PREVIOUS
    // TODO: Add the SQL/business logic for PREVIOUS on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object previousWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards PREVIOUS SQL is not implemented yet.");
    }

    // Wards - SAVE
    // TODO: Add the SQL/business logic for SAVE on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object saveWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards SAVE SQL is not implemented yet.");
    }

    // Wards - FIND
    // TODO: Add the SQL/business logic for FIND on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object findWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards FIND SQL is not implemented yet.");
    }

    // Wards - DELETE
    // TODO: Add the SQL/business logic for DELETE on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object deleteWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards DELETE SQL is not implemented yet.");
    }

    // Wards - UPDATE
    // TODO: Add the SQL/business logic for UPDATE on Wards.
    // Use dto.getString("column_name"), dto.getInteger("column_name"),
    // dto.getBigDecimal("column_name"), etc. to read submitted GUI values.
    // For SAVE, normally omit AUTO_INCREMENT primary keys.
    // For UPDATE/DELETE/FIND, include the primary key or search criteria in dto.
    // For FIRST/LAST/PREVIOUS, return the selected record as DataDTO;
    // PREVIOUS may need a current record ID supplied in dto.
    public Object updateWards(DataDTO dto) throws Exception {
        throw new UnsupportedOperationException("Wards UPDATE SQL is not implemented yet.");
    }

}

/** Handles all database operations for the Login module. */
class LoginService {

    public Object execute(String operation, DataDTO dto) throws Exception {
        switch (operation) {
            case "SAVE": return save(dto);
            case "UPDATE": return update(dto);
            case "LOGIN": return login(dto);
            case "CHECK_USER": return checkUser(dto);
            default: throw new IllegalArgumentException("Unsupported operation for LOGIN: " + operation);
        }
    }

    private Object save(DataDTO dto) throws Exception {
        String sql = "INSERT INTO login(username, password) VALUES (?, ?);";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("username"));
            pst.setString(2, dto.getString("password"));
            return pst.executeUpdate();
        }
    }

    private Object update(DataDTO dto) throws Exception {
        String sql = "UPDATE login SET password = ? WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("password"));
            pst.setString(2, dto.getString("username"));
            return pst.executeUpdate();
        }
    }

    private Object login(DataDTO dto) throws Exception {
        String sql = "SELECT Username, Password FROM login WHERE username = ?;";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("username"));
            try (ResultSet rs = pst.executeQuery()) {
                DataDTO result = new DataDTO();
                boolean authenticated = rs.next()
                        && dto.getString("password") != null
                        && dto.getString("password").equals(rs.getString("password"));
                result.put("authenticated", authenticated);
                if (authenticated) result.put("username", rs.getString("Username"));
                return result;
            }
        }
    }

    private Object checkUser(DataDTO dto) throws Exception {
        String sql = "SELECT username, password FROM login WHERE username = ?;";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("username"));
            try (ResultSet rs = pst.executeQuery()) {
                DataDTO result = new DataDTO();
                result.put("exists", rs.next());
                return result;
            }
        }
    }
}

/** Handles all database operations for the Patients module. */
class PatientsService {

    public Object execute(String operation, DataDTO dto) throws Exception {
        switch (operation) {
            case "LOAD": return load();
            case "SAVE": return save(dto);
            case "FIND": return find(dto);
            case "DELETE": return delete(dto);
            default: throw new IllegalArgumentException("Unsupported operation for PATIENTS: " + operation);
        }
    }

    private ArrayList<DataDTO> load() throws Exception {
        String sql = "SELECT * FROM patients ORDER BY patient_id";
        ArrayList<DataDTO> patients = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                DataDTO patient = new DataDTO();
                patient.put("patient_id", rs.getInt("patient_id"));
                patient.put("first_name", rs.getString("first_name"));
                patient.put("last_name", rs.getString("last_name"));
                patient.put("gender", rs.getString("gender"));
                patient.put("date_of_birth", rs.getDate("date_of_birth"));
                patient.put("phone", rs.getString("phone"));
                patient.put("address", rs.getString("address"));
                patients.add(patient);
            }
        }
        return patients;
    }

    private Object save(DataDTO dto) throws Exception {
        String sql = "INSERT INTO patients(first_name, last_name, gender, date_of_birth, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("first_name"));
            pst.setString(2, dto.getString("last_name"));
            pst.setString(3, dto.getString("gender"));
            Object dob = dto.get("date_of_birth");
            if (dob instanceof Date) {
                pst.setDate(4, (Date) dob);
            } else if (dob instanceof java.util.Date) {
                pst.setDate(4, new Date(((java.util.Date) dob).getTime()));
            } else {
                pst.setDate(4, null);
            }
            pst.setString(5, dto.getString("phone"));
            pst.setString(6, dto.getString("address"));
            return pst.executeUpdate();
        }
    }

    private Object find(DataDTO dto) throws Exception {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("patient_id"));
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) return null;
                DataDTO patient = new DataDTO();
                patient.put("patient_id", rs.getInt("patient_id"));
                patient.put("first_name", rs.getString("first_name"));
                patient.put("last_name", rs.getString("last_name"));
                patient.put("gender", rs.getString("gender"));
                patient.put("date_of_birth", rs.getDate("date_of_birth"));
                patient.put("phone", rs.getString("phone"));
                patient.put("address", rs.getString("address"));
                return patient;
            }
        }
    }

    private Object delete(DataDTO dto) throws Exception {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dto.getString("patient_id"));
            return pst.executeUpdate();
        }
    }
}

/**
 * Common parent for modules whose SQL methods are still
 * located in HospitalService.
 */
abstract class ModuleService {

    protected HospitalService hospitalService;
    private String expectedEntity;

    public ModuleService(HospitalService hospitalService,
                         String expectedEntity) {
        this.hospitalService = hospitalService;
        this.expectedEntity = expectedEntity;
    }

    public Object execute(String entity, String operation,
                          DataDTO dto) throws Exception {

        if (!expectedEntity.equals(entity)) {
            throw new IllegalArgumentException(
                    "Invalid entity for " + expectedEntity);
        }

        return hospitalService.executeEntity(
                entity, operation, dto);
    }
}

class AdmissionsService extends ModuleService {
    public AdmissionsService(HospitalService service) {
        super(service, "ADMISSIONS");
    }
}

class AppointmentsService extends ModuleService {
    public AppointmentsService(HospitalService service) {
        super(service, "APPOINTMENTS");
    }
}

class BillsService extends ModuleService {
    public BillsService(HospitalService service) {
        super(service, "BILLS");
    }
}

class DepartmentsService extends ModuleService {
    public DepartmentsService(HospitalService service) {
        super(service, "DEPARTMENTS");
    }
}

class DiagnosesService extends ModuleService {
    public DiagnosesService(HospitalService service) {
        super(service, "DIAGNOSES");
    }
}

class DoctorsService extends ModuleService {
    public DoctorsService(HospitalService service) {
        super(service, "DOCTORS");
    }
}

class LabTestsService extends ModuleService {
    public LabTestsService(HospitalService service) {
        super(service, "LAB_TESTS");
    }
}

class MedicinesService extends ModuleService {
    public MedicinesService(HospitalService service) {
        super(service, "MEDICINES");
    }
}

class NursesService extends ModuleService {
    public NursesService(HospitalService service) {
        super(service, "NURSES");
    }
}

class PaymentsService extends ModuleService {
    public PaymentsService(HospitalService service) {
        super(service, "PAYMENTS");
    }
}

class PrescriptionItemsService extends ModuleService {
    public PrescriptionItemsService(HospitalService service) {
        super(service, "PRESCRIPTION_ITEMS");
    }
}

class PrescriptionsService extends ModuleService {
    public PrescriptionsService(HospitalService service) {
        super(service, "PRESCRIPTIONS");
    }
}

class RoomsService extends ModuleService {
    public RoomsService(HospitalService service) {
        super(service, "ROOMS");
    }
}

class WardsService extends ModuleService {
    public WardsService(HospitalService service) {
        super(service, "WARDS");
    }
}
