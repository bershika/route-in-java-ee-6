package bershika.route.repository;

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

import bershika.route.entities.MemberEntity;

@RequestScoped
public class MemberListProducer {
   @Inject
   private EntityManager em;

   private List<MemberEntity> members;

   // @Named provides access the return value via the EL variable name "members" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<MemberEntity> getMembers() {
      return members;
   }

   public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final MemberEntity member) {
      retrieveAllMembersOrderedByName();
   }

   @PostConstruct
   public void retrieveAllMembersOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<MemberEntity> criteria = cb.createQuery(MemberEntity.class);
      Root<MemberEntity> member = criteria.from(MemberEntity.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
      criteria.select(member).orderBy(cb.asc(member.get("name")));
      members = em.createQuery(criteria).getResultList();
   }
}
