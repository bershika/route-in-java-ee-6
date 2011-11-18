package bershika.route.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bershika.route.model.Hub;

@RequestScoped
public class HubListProducer {
	@Inject
	private EntityManager em;

	private List<Hub> hubs;

	@Produces
	@Named
	public List<Hub> getHubs() {
		return hubs;
	}

	public void onHubListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final Hub hub) {
		retrieveAllHubsOrderedByName();
	}

	@PostConstruct
	public void retrieveAllHubsOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Hub> criteria = cb.createQuery(Hub.class);
		Root<Hub> hub = criteria.from(Hub.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
		criteria.select(hub).orderBy(cb.asc(hub.get("state")));
		hubs = em.createQuery(criteria).getResultList();
	}

}
