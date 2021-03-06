package cz.muni.fi.pa165.team;

import cz.muni.fi.pa165.team.exceptions.GoalNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * This class implements the TeamMatchGoalRepository interface.
 *
 * @author Tomas Smid <smid.thomas@gmail.com>
 */
@Repository
public class TeamMatchGoalRepositoryImpl implements TeamMatchGoalRepository
{

    private EntityManager em;

    public TeamMatchGoalRepositoryImpl(EntityManager em)
    {
        this.em = em;
    }

    @Override
    public TeamMatchGoal getGoalById(final UUID goalId)
    {
        Assert.notNull(goalId, "Cannot search for a null goal id");

        try {
            return em.createQuery("SELECT g FROM TeamMatchGoal g WHERE g.id = :goalId", TeamMatchGoal.class)
                .setParameter("goalId", goalId)
                .getSingleResult();
        } catch (NoResultException ex) {
            throw new GoalNotFoundException(goalId, ex);
        }
    }

    @Override
    public Collection<TeamMatchGoal> findGoalByScorer(final UUID scorerId)
    {
        Assert.notNull(scorerId, "Cannot search for null scorer");

        return em.createQuery("SELECT g FROM TeamMatchGoal g WHERE g.scorer.id = :scorerid", TeamMatchGoal.class)
            .setParameter("scorerid", scorerId)
            .getResultList();
    }

    @Override
    public Collection<TeamMatchGoal> findGoalByAssistant(final UUID assistantId)
    {
        Assert.notNull(assistantId, "Cannot search for null assistant");

        return em.createQuery("SELECT g FROM TeamMatchGoal g WHERE g.assistant.id = :assistantid", TeamMatchGoal.class)
            .setParameter("assistantid", assistantId)
            .getResultList();
    }

    @Override
    public Collection<TeamMatchGoal> findGoalByMatch(final UUID matchId)
    {
        Assert.notNull(matchId, "Cannot search for null match");

        return em.createQuery("SELECT g FROM TeamMatchGoal g WHERE g.match.id = :matchid", TeamMatchGoal.class)
            .setParameter("matchid", matchId)
            .getResultList();
    }

    @Override
    public Collection<TeamMatchGoal> findAllGoals()
    {
        return em.createQuery("SELECT g FROM TeamMatchGoal g", TeamMatchGoal.class).getResultList();
    }

    @Override
    public TeamMatchGoal findConflictingGoal(final UUID matchId, final UUID scorerId, final UUID assistantId, final Date matchTime)
    {
        Assert.notNull(matchId, "Cannot search goal for a null match");
        Assert.notNull(scorerId, "Cannot search goal for a null scorer");
        Assert.notNull(assistantId, "Cannot search goal for a null assistant");
        Assert.notNull(matchTime, "Cannot search goal for a null goal match time");

        try {
            return em.createQuery("SELECT g FROM TeamMatchGoal g WHERE g.match.id = :matchId AND " +
                "g.scorer.id = :scorerId AND g.assistant.id = :assistantId AND g.matchTime = :matchTime", TeamMatchGoal.class)
                .setParameter("matchId", matchId)
                .setParameter("scorerId", scorerId)
                .setParameter("assistantId", assistantId)
                .setParameter("matchTime", matchTime)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
