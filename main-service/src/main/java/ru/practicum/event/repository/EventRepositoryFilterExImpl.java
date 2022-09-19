package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;
import ru.practicum.event.type.EventSort;
import ru.practicum.event.type.EventState;
import ru.practicum.request.model.Request;
import ru.practicum.request.type.RequestStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventRepositoryFilterExImpl implements EventRepositoryFilterEx {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findEx(EventFilterParams filterParams, EventSort sort, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);

        Predicate conditions = createFilter(root, cb, query, filterParams);

        query.select(root).where(conditions);

        if ((sort == null) || (sort == EventSort.EVENT_DATE)) {
            query.orderBy(cb.asc(root.get("eventDate")));
        }

        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

    }

    private Predicate createFilterByText(Root<Event> root, CriteriaBuilder cb, String text) {

        // Поиск по annotation или description без учета регистра букв

        String value = "%" + text.toLowerCase() + "%";

        Predicate predicateAnnotation = cb.like(cb.lower(root.get("annotation")), value);
        Predicate predicateDescription = cb.like(cb.lower(root.get("description")), value);

        return cb.or(predicateAnnotation, predicateDescription);

    }

    private Predicate createFilterByCategories(Root<Event> root, CriteriaBuilder cb,
                                               Long[] categories) {

        Predicate[] predicates = new Predicate[categories.length];

        for (int i = 0; i < categories.length; i++) {
            predicates[i] = cb.equal(root.get("category").get("id"), categories[i]);
        }

        return cb.or(predicates);
    }

    private Predicate createFilterByPaid(Root<Event> root, CriteriaBuilder cb, Boolean value) {
        return cb.equal(root.get("paid"), value);
    }

    private Predicate createFilterByDefaultPeriod(Root<Event> root, CriteriaBuilder cb) {
        return cb.greaterThan(root.get("eventDate"), LocalDateTime.now());
    }

    private Predicate createFilterByRangeStart(Root<Event> root, CriteriaBuilder cb,
                                               LocalDateTime rangeStart) {
        return cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
    }

    private Predicate createFilterByRangeEnd(Root<Event> root, CriteriaBuilder cb,
                                             LocalDateTime rangeEnd) {
        return cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
    }

    private Predicate createFilterByAvailable(Root<Event> root, CriteriaBuilder cb,
                                              CriteriaQuery<Event> query) {

        Subquery requestQuery = query.subquery(Long.class);
        Root<Request> requestRoot = requestQuery.from(Request.class);

        Predicate p1 = cb.equal(requestRoot.get("eventId"), root.get("id"));
        Predicate p2 = cb.equal(requestRoot.get("status"), RequestStatus.CONFIRMED);

        requestQuery.select(cb.count(requestRoot.get("id"))).where(cb.and(p1, p2));

        Predicate p3 = cb.lessThan(requestQuery, root.get("participantLimit"));
        Predicate p4 = cb.equal(root.get("participantLimit"), 0);

        return cb.or(p3, p4);

    }

    private Predicate createFilterByUsers(Root<Event> root, CriteriaBuilder cb,
                                          Long[] users) {

        Predicate[] predicates = new Predicate[users.length];

        for (int i = 0; i < users.length; i++) {
            predicates[i] = cb.equal(root.get("owner").get("id"), users[i]);
        }

        return cb.or(predicates);
    }

    private Predicate createFilterByStates(Root<Event> root, CriteriaBuilder cb,
                                           EventState[] states) {

        Predicate[] predicates = new Predicate[states.length];

        for (int i = 0; i < states.length; i++) {
            predicates[i] = cb.equal(root.get("state"), states[i]);
        }

        return cb.or(predicates);
    }

    private Predicate createFilter(Root<Event> root, CriteriaBuilder cb,
                                   CriteriaQuery<Event> query, EventFilterParams filterParams) {

        List<Predicate> predicateList = new ArrayList<>();

        if (filterParams.getText() != null) {
            predicateList.add(createFilterByText(root, cb, filterParams.getText()));
        }

        if ((filterParams.getCategories() != null) && (filterParams.getCategories().length > 0)) {
            predicateList.add(createFilterByCategories(root, cb, filterParams.getCategories()));
        }

        if (filterParams.getPaid() != null) {
            predicateList.add(createFilterByPaid(root, cb, filterParams.getPaid()));
        }

        if ((filterParams.getRangeStart() == null) && (filterParams.getRangeEnd() == null)) {
            predicateList.add(createFilterByDefaultPeriod(root, cb));
        }

        if (filterParams.getRangeStart() != null) {
            predicateList.add(createFilterByRangeStart(root, cb, filterParams.getRangeStart()));
        }

        if (filterParams.getRangeEnd() != null) {
            predicateList.add(createFilterByRangeEnd(root, cb, filterParams.getRangeEnd()));
        }

        if ((filterParams.getOnlyAvailable() != null) && (filterParams.getOnlyAvailable())) {
            predicateList.add(createFilterByAvailable(root, cb, query));
        }

        if ((filterParams.getUsers() != null) && (filterParams.getUsers().length > 0)) {
            predicateList.add(createFilterByUsers(root, cb, filterParams.getUsers()));
        }

        if ((filterParams.getStates() != null) && (filterParams.getStates().length > 0)) {
            predicateList.add(createFilterByStates(root, cb, filterParams.getStates()));
        }

        return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

    }

}
