package py.com.progweb.api.rest;

import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.dto.CreateUsedPoints;
import py.com.progweb.api.ejb.ClientDAO;
import py.com.progweb.api.ejb.ConceptDAO;
import py.com.progweb.api.ejb.PointBagDAO;

import py.com.progweb.api.ejb.PointUseDAO;
import py.com.progweb.api.ejb.PointUseDetailDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.Client;
import py.com.progweb.api.model.ConceptPointUse;
import py.com.progweb.api.model.PointBag;
import py.com.progweb.api.model.PointUse;
import py.com.progweb.api.model.PointUseDetail;

@Path("points_used")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PointUseRest {
	@Inject
	PointUseDAO pointUseDao;

    @Inject
    ClientDAO clientDao;

    @Inject
    ConceptDAO conceptDao;

    @Inject
    PointBagDAO pointBagDao;

    @Inject
    PointUseDetailDAO pointUseDetailDao;

	@GET
	@Path("/")
	public Response getAll() {
		return Response.ok(pointUseDao.getAll()).build();
	}

    @POST
    @Path("/")
    public Response create(CreateUsedPoints body) throws ApiException, MessagingException {
        Integer clientId = body.getClientId();
        Integer conceptPointUseId = body.getConceptPointUseId();
        PointUse pointUse = new PointUse();
        Client client = clientDao.getById(clientId);
        Set<PointBag> pointBags = client.getListPointBag();
        List<PointBag> listPointBag = new ArrayList<PointBag>(client.getListPointBag());
        Collections.sort(listPointBag, new expirationDateComparator());
        int totalPoints=this.totalPoints(pointBags);
        ConceptPointUse concept = conceptDao.getById(conceptPointUseId);
        int remainsPoints = totalPoints - concept.getPoints();
        if (concept == null){
            throw new ApiException ("No existe el concepto",422);
        }

        if (totalPoints < concept.getPoints()){
            throw new ApiException ("No existen puntos suficientes",500);
        }

        pointUse.setClient(client);
        pointUse.setConcept(concept);
        pointUse.setUsed_points(concept.getPoints());

        int totalPointsConcept=concept.getPoints();

        pointUse.setDate(PointUseRest.getDateWithoutTimeUsingCalendar());

        pointUse = pointUseDao.create(pointUse);

        Set<PointUseDetail> details = new HashSet<>();

        for (PointBag pointBag : listPointBag) {
            int pointsBag = pointBag.getPointsBalance();
            PointBag pointBagNew = pointBag;
            int usedPointsBag;
            if (pointsBag>totalPointsConcept){
                pointsBag = pointsBag - totalPointsConcept;
                usedPointsBag=totalPointsConcept;
                totalPointsConcept=0;
            } else {
                totalPointsConcept = totalPointsConcept - pointsBag;
                usedPointsBag=pointsBag;
                pointsBag=0;
            }

            if (usedPointsBag>0) {
                pointBagNew.setPointsBalance(pointsBag);
                pointBagNew.setUsedPoints(pointBagNew.getPoints()-pointsBag);

                pointBagNew = pointBagDao.update(pointBagNew.getId(), pointBagNew);

                PointUseDetail pointUseDetail = new PointUseDetail();
                pointUseDetail.setPointBag(pointBagNew);
                pointUseDetail.setPointUse(pointUse);
                pointUseDetail.setUsedPoints(usedPointsBag);

                pointUseDetail = pointUseDetailDao.create(pointUseDetail);
                details.add(pointUseDetail);
            }
        }
        pointUse.setDetails(details);
        PointUseRest.sendEmail(client,pointUse,remainsPoints);
        return Response.ok(pointUse).build();
    }

    class expirationDateComparator implements java.util.Comparator<PointBag> {
        @Override
        public int compare(PointBag a, PointBag b) {
            return a.getExpirationDate().compareTo(b.getExpirationDate()) ;
        }
    }

    //TODO: repeated code
    public static Date getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int sendEmail(Client client, PointUse pointUse, int remains) throws AddressException, MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.office365.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");

        Session session=Session.getDefaultInstance(props);

        String mailSender = "backendsiuu@outlook.com";
        String passwordSender = "olimpiatupapa3";
        String mailReceiver = client.getEmail();
        String subject = "Comprobante";
        String messageContent = "Backend\n"
                +"Puntos Usados: "+pointUse.getUsed_points()+"\n"
                +"Concepto: "+pointUse.getConcept().getDescription()+"\n"
                +"Puntos restantes: "+remains;

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailSender));
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(mailReceiver));
        message.setSubject(subject);
        message.setText(messageContent);

        try (Transport t = session.getTransport("smtp")) {
            t.connect(mailSender,passwordSender);
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        }
        return 0;
    }

    public Integer totalPoints (Set<PointBag> listPointBag){
        int total=0;

        for(PointBag pointBag : listPointBag){
            total=total + pointBag.getPointsBalance();
        }
        return total;
    }

	@GET
	@Path("/concept/{id}")
	public Response getByConcept(@PathParam("id") Integer id) {
		return Response.ok(pointUseDao.getByConcept(id)).build();
	}

	@GET
	@Path("/client/{id}")
	public Response getByClient(@PathParam("id") Integer id) {
		return Response.ok(pointUseDao.getByClient(id)).build();
	}

	@GET
	@Path("/used_date/{date}")
	public Response getByUsedDate(@PathParam("date") String date) throws ApiException {
		try {
			Date dateFormatted= new SimpleDateFormat("yyyy-MM-dd").parse(date);
			return Response.ok(pointUseDao.getByUsedDate(dateFormatted)).build();
		} catch (Exception e) {
			throw new ApiException("Incorrect Date", 500);
		}
	}
}
