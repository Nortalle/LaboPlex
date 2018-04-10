// Auteur: Eric Lefrançois - Février. 2015
package controllers;

import java.util.*;

// Du jar hibernate-core-4.3.5.Final
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

// Pour les chargements "EAGER"
import org.hibernate.FetchMode;

import models.*;
import views.*;
import ch.heigvd.iict.ser.imdb.models.*;

//------------------------------------------------------------------------------
public class ORMAccess  {

	private static SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	public ORMAccess() {
		sessionFactory = new Configuration()
		// Configure le mapping ORM en analysant
		// le contenu de hibernate.cfg.xml
		.configure("hibernate.cfg.xml")
		.buildSessionFactory();
	}

	@SuppressWarnings("all") 
	public GlobalData GET_GLOBAL_DATA() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		GlobalData globalData = new GlobalData();
		try {
			tx = session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Projection> projections = session.createQuery( "select distinct p from Projection as p left join fetch p.salle left join fetch p.film" ).list();

			// Code ci-dessous pour charger tous les objets enfants
			for (Projection p: projections){
				Set<RoleActeur> roles = p.getFilm().getRoles();
				for (RoleActeur role: roles){
					//String acteur = role.getActeur().getNom();
				}
				for (Genre genre: p.getFilm().getGenres()){
					//String label = genre.getLabel();
				}
				for (Motcle motcle: p.getFilm().getMotcles()){
					//String label  =motcle.getLabel();
				}
				for (Langage langage: p.getFilm().getLangages()){
					//String label = langage.getLabel();
				}
			}
			globalData.setProjections(projections);

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}

		return globalData;
	}

	public void initBD(Data data, MainGUI mainGUI) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {

			tx= session.beginTransaction();
			viderDonneesCinematographiques(mainGUI, session);
			viderDonneesDessalles(mainGUI, session);
			peuplerLaBaseAvecSalles(mainGUI, session);
			peuplerLaBaseAvecDonneesWFC(data, mainGUI, session);
			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
	}

	public void initBDAvecNouvellesVersionWFC(Data data, MainGUI mainGUI) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {

			tx= session.beginTransaction();
			viderDonneesCinematographiques(mainGUI, session);
			peuplerLaBaseAvecDonneesWFC(data, mainGUI, session);
			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
	}


	private void viderDonneesDessalles (MainGUI mainGUI, Session session) throws Exception {
		Query query = session.createQuery("delete from Salle"); query.executeUpdate(); 
	}

	private void viderDonneesCinematographiques (MainGUI mainGUI, Session session) throws Exception {
		mainGUI.setAcknoledgeMessage("Creation des salles... ");

		Query query = session.createQuery("delete from Projection"); query.executeUpdate(); 
		query = session.createQuery("delete from RoleActeur"); query.executeUpdate();  
		query = session.createQuery("delete from Langage"); query.executeUpdate();
		query = session.createQuery("delete from Critique"); query.executeUpdate();
		query = session.createQuery("delete from Motcle"); query.executeUpdate();
		query = session.createQuery("delete from Genre"); query.executeUpdate(); 
		query = session.createQuery("delete from Film"); query.executeUpdate();  
		query = session.createQuery("delete from Acteur"); query.executeUpdate();  
	}

	private void peuplerLaBaseAvecSalles (MainGUI mainGUI, Session session) throws Exception {
		mainGUI.setAcknoledgeMessage("Creation des salles... ");

		session.save(new Salle(200, "Flon 1"));
		session.save(new Salle(300, "Flon 2"));
		session.save(new Salle(450, "Flon 3"));
	}

	private void peuplerLaBaseAvecDonneesWFC(Data data, MainGUI mainGUI, Session session) throws Exception {

		// Enregistrer les films
		System.out.println("Enregistrement des films ... ");
		mainGUI.setAcknoledgeMessage("Enregistrement des films ... ");
		Map<Long, Movie> movies = data.getMovies();
		for (Long key : movies.keySet()){
			Movie movie = movies.get(key);
			Set<String> genres = movie.getGenres();
			Set<String> motcles = movie.getKeywords();
			Set<String> langages = movie.getLanguages();
			Film film = new Film(movie.getTitle(), movie.getPlot(), movie.getRuntime());
			film.setId(movie.getId());
			for (String unGenre: genres){
				film.ajouterGenre(new Genre(unGenre));
			}
			for (String unMotcle: motcles){
				film.ajouterMotcle(new Motcle(unMotcle));
			}
			for (String unLangage: langages){
				film.ajouterLangage(new Langage(unLangage));
			}
			session.save(film);
		}
		System.out.println("Enregistrement des films: OK");

		// Enregistrer les acteurs
		System.out.println("Enregistrement des acteurs ... ");
		mainGUI.setAcknoledgeMessage("Enregistrement des acteurs ... ");
		Map<Long, Person> persons = data.getPersons();
		for (Long key : persons.keySet()){
			Person person = persons.get(key);
			Sexe sexe = null;
			switch (person.getGender()) {
			case MALE: sexe = Sexe.MASCULIN; break;
			case FEMALE: sexe = Sexe.FEMININ; break;
			case UNKNOWN: sexe = Sexe.INCONNU; break;        
			}
			Calendar birthDate = Calendar.getInstance();
			if (person.getBirthDate() != null) 
				birthDate.setTime(person.getBirthDate());
			else
				birthDate = null;
			Calendar deathDate = Calendar.getInstance();
			if (person.getDeathDate() != null) 
				deathDate.setTime(person.getDeathDate());
			else
				deathDate = null;

			Acteur acteur = new Acteur(person.getName(), person.getBirthName(), sexe, 
					birthDate, deathDate, person.getBiography());
			acteur.setId(person.getId());
			session.save(acteur);
		}
		System.out.println("Enregistrement des acteurs: OK");

		// Enregistrer les rôles
		System.out.println("Enregistrement des roles ... ");
		mainGUI.setAcknoledgeMessage("Enregistrement des roles ... ");
		for (Long key : movies.keySet()){
			Movie movie = movies.get(key);
			Set<Role> roles = movie.getActorsIds();
			for (Role role: roles){
				Acteur acteur= (Acteur)session.createCriteria(Acteur.class)
						.add(Restrictions.eq("id", role.getActorId()))
						.uniqueResult();
				RoleActeur roleDeLacteur = new RoleActeur(acteur, role.getOrder(), role.getCharacter());
				Film film= (Film)session.createCriteria(Film.class)
						.add(Restrictions.eq("id", movie.getId()))
						.uniqueResult();
				film.ajouterRole(roleDeLacteur);
			}

		}
		System.out.println("Enregistrement des roles: OK");
		mainGUI.setAcknoledgeMessage("Enregistrement des roles: OK");
	}


	@SuppressWarnings("unchecked")
	public List<Projection> GET_PROJECTIONS() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Projection> projections = null;
		try {

			tx= session.beginTransaction();

			projections = session.createQuery( "from Projection as p left join fetch p.salle as s left join fetch p.film order by s.no, p.dateHeure" ).list();

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}

		return projections;
	}

	public Projection GET_PROJECTION(long projectionId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Projection projection = null;
		try {

			tx= session.beginTransaction();

			projection = (Projection)session.createCriteria(Projection.class)
					.setFetchMode("salle", FetchMode.JOIN) // Charger la salle par la même occasion
					.setFetchMode("film", FetchMode.JOIN)  // Charger le film par la même occasion
					.add(Restrictions.eq("id", projectionId))
					.uniqueResult();

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		return projection;
	}


	public void SAVE_PROJECTION(Projection projection) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {

			tx= session.beginTransaction();

			session.save(projection);

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
	}

	public void UPDATE_PROJECTION(Projection projection) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {

			tx= session.beginTransaction();

			session.update(projection);

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
	}



	public Projection DELETE_PROJECTION(long projectionId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Projection projection = null;
		try {

			tx= session.beginTransaction();

			projection = (Projection)session.createCriteria(Projection.class)
					.add(Restrictions.eq("id", projectionId))
					.uniqueResult();

			session.delete(projection);

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		return projection;
	}

	@SuppressWarnings("unchecked")
	public List<Film> GET_FILMS() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Film> films = null;
		try {

			tx = session.beginTransaction();

			films = session.createQuery( "from Film" ).list(); 

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}

		return films;
	}


	public Film GET_FILM(long filmId) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Film film = null;
		try {

			tx= session.beginTransaction();

			film = (Film)session.createCriteria(Film.class)
					.setFetchMode("critiques", FetchMode.JOIN) // Charger les critiques par la même occasion
					.add(Restrictions.eq("id", filmId))
					.uniqueResult();  // Enlève les doublons au passage (dû au JOIN)

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		return film;
	}


	public Film GET_FILM_AVEC_TITRE(String titreFilm) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Film film = null;
		try {

			tx= session.beginTransaction();

			film = (Film)session.createCriteria(Film.class)
					.add(Restrictions.eq("titre", titreFilm))
					.uniqueResult();

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		return film;
	}

	@SuppressWarnings("unchecked")
	public List<String> GET_TITRES_FILMS() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Object> films = null;  // Chaque ligne retournée contient une seule colonne
		List<String> titresFilms = new ArrayList<String>();
		try {

			tx= session.beginTransaction();

			films = session.createQuery( "select f.titre from Film as f" ).list();

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		Iterator<Object> filmIterator = films.iterator(); 
		while (filmIterator.hasNext() ) { 
			String titre = (String)filmIterator.next(); 
			titresFilms.add(titre);
		}

		return titresFilms;
	}

	public void UPDATE_FILM(Film film) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {

			tx= session.beginTransaction();

			session.update(film);

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
	}

	@SuppressWarnings("unchecked")
	public Salle GET_SALLE(String noSalle) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Salle> salles = null;
		try {

			tx= session.beginTransaction();

			Query query = session.createQuery( "select s from Salle as s where s.no = :noSalle" );

			query.setString("noSalle", noSalle);

			salles = query.list();
			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		return salles.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<String> GET_NOS_DES_SALLES() throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Object> salles = null;  // Chaque ligne retournée contient une seule colonne
		List<String> nosSalles = new ArrayList<String>();
		try {

			tx= session.beginTransaction();

			Query query = session.createQuery( "select distinct s.no from Salle as s" );
			salles = query.list();

			tx.commit();
		}
		catch (Exception e) {catchException(e, tx);}
		finally {doFinally(session);}
		Iterator<Object> sallesIterator = salles.iterator(); 
		while (sallesIterator.hasNext() ) { 
			String no = (String)sallesIterator.next(); 
			nosSalles.add(no);
		}

		return nosSalles;
	}


	public static void terminate() {
		try {
			if ( sessionFactory != null ) {
				sessionFactory.close();
			}
		}
		catch(Exception e) {
			System.out.println (e.toString());
		}      
	}

	private void catchException(Exception e, Transaction tx) throws Exception {
		if (tx!= null){
			try {
				tx.rollback();
				System.out.println(e.toString());
				throw e;
			}
			catch (HibernateException he){
				System.out.println(he.toString());
				throw he;
			}
		}
	}

	private void doFinally(Session session) {
		try{
			session.close();
		}
		catch (HibernateException he){
			System.out.println(he.toString());
		}
	}
}