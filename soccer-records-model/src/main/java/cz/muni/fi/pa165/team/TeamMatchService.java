package cz.muni.fi.pa165.team;

import cz.muni.fi.pa165.team.exceptions.GoalWithSameParametersAlreadyExistsException;
import cz.muni.fi.pa165.team.exceptions.MatchTimeCollisionException;
import cz.muni.fi.pa165.team.exceptions.MatchWithSameParametersAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author Tomas Smid <smid.thomas@gmail.com>
 */
public class TeamMatchService
{

    public TeamMatchService(){

    }

    public TeamMatch createMatch(
        List<TeamMatch> allMatchesOfHomeTeam,
        List<TeamMatch> allMatchesOfAwayTeam,
        Team homeTeam,
        Team awayTeam,
        Date startTime,
        Date endTime
    )
    {
        Assert.notNull(homeTeam, "Match cannot be created with a null home team");
        Assert.notNull(awayTeam, "Match cannot be created with a null away team");
        Assert.isTrue(!homeTeam.equals(awayTeam), "Match cannot be created for home and away teams which are same");

        validateMatchTimes(startTime, endTime);

        if(!allMatchesOfHomeTeam.isEmpty())
        {
            for(TeamMatch tm : allMatchesOfHomeTeam)
            {
                if (startTime.equals(tm.getStartTime()))
                {
                    throw new MatchWithSameParametersAlreadyExistsException(
                        homeTeam.getId(),
                        startTime
                    );
                }
            }
        }

        if(!allMatchesOfAwayTeam.isEmpty())
        {
            for(TeamMatch tm : allMatchesOfAwayTeam)
            {
                if (startTime.equals(tm.getStartTime()))
                {
                    throw new MatchWithSameParametersAlreadyExistsException(
                        awayTeam.getId(),
                        startTime
                    );
                }
            }
        }

        return new TeamMatch(homeTeam,awayTeam,startTime,endTime);
    }

    public void changeMatchTime(
        List<TeamMatch> sameStartTimeMatches,
        TeamMatch match,
        Date startTime,
        Date endTime
    )
    {
        Assert.notNull(match, "Cannot change match times to null match");
        validateMatchTimes(startTime, endTime);

        if (!sameStartTimeMatches.isEmpty()){
            for (TeamMatch tm : sameStartTimeMatches)
            {
                if (match.getHomeTeam().equals(tm.getHomeTeam()) ||
                    match.getHomeTeam().equals(tm.getAwayTeam()))
                {
                    throw new MatchTimeCollisionException(
                        match.getId(),
                        tm.getId(),
                        match.getHomeTeam().getId(),
                        startTime
                    );
                }
                if (match.getAwayTeam().equals(tm.getAwayTeam()) ||
                    match.getAwayTeam().equals(tm.getHomeTeam()))
                {
                    throw new MatchTimeCollisionException(
                        match.getId(),
                        tm.getId(),
                        match.getAwayTeam().getId(),
                        startTime
                    );
                }
            }
        }

        match.changeMatchTime(startTime, endTime);
    }

    public void endMatch(TeamMatch match, Date endTime)
    {
        Assert.notNull(match, "Cannot end the null match");
        Assert.notNull(endTime, "Cannot end the match with a null end time");
        Assert.isNull(match.getEndTime(), "Cannot end already ended match");

        validateMatchTimes(match.getStartTime(),endTime);

        match.endMatch(endTime);
    }

    public TeamMatchGoal addNewScoredGoal(
        List<TeamMatchGoal> goalsInActualMatch,
        TeamPlayer scorer,
        TeamPlayer assistant,
        TeamMatch match,
        Date matchTime
    )
    {
        Assert.notNull(scorer, "Cannot create new goal with a null scorer");
        Assert.notNull(assistant, "Cannot create new goal with a null assistant");
        Assert.isTrue(!scorer.equals(assistant), "Cannot create new goal with scorer and assistant who are the same player");

        validateGoalMatchTime(matchTime,match);

        if (!goalsInActualMatch.isEmpty())
        {
            for(TeamMatchGoal tmg : goalsInActualMatch)
            {
                if(tmg.getScorer().equals(scorer) && tmg.getAssistant().equals(assistant)
                    && tmg.getMatchTime().equals(matchTime))
                {
                    throw new GoalWithSameParametersAlreadyExistsException(
                        scorer.getId(),
                        assistant.getId(),
                        match.getId(),
                        matchTime
                    );
                }
            }
        }

        return new TeamMatchGoal(scorer,assistant,match,matchTime);
    }

    private void validateMatchTimes(Date startTime, Date endTime)
    {
        Assert.notNull(startTime, "Match start time is null");
        Assert.isTrue((endTime == null || endTime.after(startTime)), "Match end time is not after start time");
    }

    private void validateGoalMatchTime(Date matchTime, TeamMatch match)
    {
        Assert.notNull(matchTime, "Cannot create new goal with a null match time");
        Assert.isTrue(matchTime.after(match.getStartTime()), "Cannot create new goal with goal match time which is not " +
                                                              "after match start time");
        Assert.isTrue(match.getEndTime() == null || matchTime.before(match.getEndTime()),
            "Cannot create new goal with goal match time which is not before match end time");
    }
}
