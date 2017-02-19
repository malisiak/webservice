package serviceDatabase.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import serviceDatabase.Entities.*;
import serviceDatabase.transferObject.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Date;

/**
 * Klasa odbierająca dane i zapisująca do bazy danych.
 */
@Path("/serwer")
public class WebApplication {


    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {
        String output = "Jersey say" + msg;
        return Response.status(200).entity(output).build();
    }

    /**
     * Metoda zapisuje dane do odpowiednich encji w bazie danych.
     * @param dataList Obiekt reprezentujący dane, które zostały przesłane do metody.
     * @return Status określa wynik wykonania operacji zapisu do danych.
     */
    @POST
    @Path("/activity")
    public Response postData(Wrapper dataList) {
        int status;
        SessionFactory sessionFactory = DBUtil.getSessionFactory();

        Long id = getUserId(dataList.getUser().getEmail());
        System.out.println("id: " + id);


        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            if (dataList.getActivities() != null) {
                for (RecognizedActivity activity : dataList.getActivities()) {
                    ActivityTable activityRecord = new ActivityTable(activity.activityType, new Date(activity.getDataStart()), activity.getTotalTime());
                    activityRecord.setUser(new UserTable());
                    activityRecord.getUser().setId(id);
                    session.save(activityRecord);
                }
            }

            if (dataList.getCalls() != null) {
                for (CallLog call : dataList.getCalls()) {
                    CallTable iocRecord = new CallTable(call.getCallType(), call.getDuration(), call.getCallerName(), new Date(call.getDate()));
                    iocRecord.setUser(new UserTable());
                    iocRecord.getUser().setId(id);
                    session.save(iocRecord);

                }
            }

            if (dataList.getSms() != null) {
                for (SmsLog sms : dataList.getSms()) {
                    SmsTable smsRecord = new SmsTable(sms.getSmsType(), sms.getPersonName(), new Date(sms.getDate()));
                    smsRecord.setUser(new UserTable());
                    smsRecord.getUser().setId(id);
                    session.save(smsRecord);
                }
            }

            if (dataList.getProcess() != null) {
                for (ProcessLog process : dataList.getProcess()) {
                    ProcessTable processRecord = new ProcessTable(process.getProcesName(), process.getTimeInForeground(), new Date(process.getDate()));
                    processRecord.setUser(new UserTable());
                    processRecord.getUser().setId(id);
                    session.save(processRecord);
                }
            }

            if (dataList.getLocation() != null) {
                for (LocationLog location : dataList.getLocation()) {
                    LocationTable locationRecord = new LocationTable(location.getLatitude(), location.getLongitude(), new Date(location.getDate()), location.getProvider(), location.getSpeed());
                    locationRecord.setUser(new UserTable());
                    locationRecord.getUser().setId(id);
                    session.save(locationRecord);

                }
            }
            session.getTransaction().commit();
            status = 200;

        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
            status = 409;
        } finally {
            session.close();
        }
        return Response.status(status).build();
    }

    /**
     * Metoda tworzy nowego użytkownika w bazie.
     * @param user Obiekt reprezentujący dane, które zostały przesłane do metody.
     * @return Status określa wynik wykonania operacji zapisu do danych.
     */
    @POST
    @Path("/register")
    public Response register(User user) {

        // nie dokonano żadnych działań
        int status = 304;
        if (!userSearch(user.getEmail())) {
            SessionFactory sessionFactory = DBUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            try {
                session.beginTransaction();
                UserTable ut = new UserTable(user.getName(), user.getEmail(), user.getPassword());
                session.save(ut);
                // utworzono
                status = 201;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                session.close();
            }

        } else {
            // conflict - uzytkownik w bazie
            status = 409;

        }
        return Response.status(status).build();
    }

    /**
     * Metoda umożliwia sprawdzenie czy użytkownik o podanym haśle i adresie e-mail istnieje w bazie danych.
     * @param user Obiekt reprezentujący dane, które zostały przesłane do metody.
      * @return Status określa wynik wykonania operacji zapisu do danych.
     */
    @POST
    @Path("/login")
    public Response login(User user) {

        int status = 304;
        if (userSearch(user.getEmail())) {
            if (userCheckPassword(user.getEmail(), user.getPassword())) {
                //OK
                status = 200;
            } else {
                // złe hasło
                status = 409;
            }

        } else {
            // nie odnaleziono
            status = 424;
        }

        return Response.status(status).build();
    }


    /**
     * Metoda sprawdza czy w bazie istnieje użytkownik o podanym adresie e-mail.
     * @param email Obiekt reprezentujący adres e-mail użytkownika
     * @return Zwrócona zostaje wartość logiczna; true oznacza wykrycie użytkownika o podanym adresie e-mail, natomiast
     * false - brak użytkownika w bazie
     */
    public boolean userSearch(String email) {

        StringBuilder sql = new StringBuilder();
        // count(u.id) - funkcja agregujaca, tworzy nową tabele wynikowa z liczbą użytkowników
        sql.append(" select  count(u.id) count from usertable u");
        sql.append(" where ");
        sql.append(" u.email like :email");
        SessionFactory sessionFactory = DBUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query query = session.createNativeQuery(sql.toString()).addScalar("count", IntegerType.INSTANCE);
        query.setParameter("email", email);
        Integer result = (Integer) query.uniqueResult();
        session.close();
        if (result.equals(1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metoda sprawdza czy podane hasło zgadza się z hasłem znajdującym sie w bazie danych
     * uzytkownika o podanym adresie e-mail.
     * @param email Obiekt reprezentujący adres e-mail użytkownika.
     * @param password Obiekt, reprezentujacy hasło użytkownika.
     * @return Zwrócona zostaje wartość logiczna - true oznacza zgodność podanego hasła z hasłem znajdującym sie w bazie ,
     * false - nie istnieje użytkownik o podanym haśle i adresie e-mail
     */
    public boolean userCheckPassword(String email, String password) {

        StringBuilder sql = new StringBuilder();
        // count(u.id) - funkcja agregujaca, tworzymy nową tabele
        sql.append(" select  count(u.id) count from usertable u");
        sql.append(" where ");
        sql.append(" u.email like :email ");
        sql.append(" and u.password like :password ");
        SessionFactory sessionFactory = DBUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query query = session.createNativeQuery(sql.toString()).addScalar("count", IntegerType.INSTANCE);
        query.setParameter("email", email);
        query.setParameter("password", password);
        Integer result = (Integer) query.uniqueResult();
        session.close();
        if (result.equals(1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metoda zwraca identyfikator użytkownika.
     * @param email Obiekt reprezentujący adres e-mail użytkownika.
     * @return Identyfikator uzytkownika.
     */
    public long getUserId(String email) {

        StringBuilder sql = new StringBuilder();
        sql.append(" select u.id from usertable u ");
        sql.append(" where ");
        sql.append(" u.email like :email");
        SessionFactory sessionFactory = DBUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query query = session.createNativeQuery(sql.toString()).addScalar("id", LongType.INSTANCE);
        query.setParameter("email", email);
        Long result = (Long) query.uniqueResult();
        session.close();
        return result;
    }
}
