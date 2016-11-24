package cz.muni.fi.pa165.team;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * @author Libor Mühlpachr <libor.muhl@seznam.cz>
 */
public class TeamPlayerFacade
{

    private TeamPlayerService teamPlayerService;

    private TeamPlayerRepository teamPlayerRepository;

    private EntityManager entityManager;

    /**
     * This constructor takes 3 parameters.
     *
     * @param teamPlayerService
     * @param teamPlayerRepository
     * @param entityManager
     */
    public TeamPlayerFacade(
        TeamPlayerService teamPlayerService,
        TeamPlayerRepository teamPlayerRepository,
        EntityManager entityManager
    )
    {
        this.teamPlayerService = teamPlayerService;
        this.teamPlayerRepository = teamPlayerRepository;
        this.entityManager = entityManager;
    }

    /**
     * This method takes 5 parameters and creates a player.
     *
     * @param teamPlayerFirstname
     * @param teamPlayerSurname
     * @param teamPlayerHeight
     * @param teamPlayerWeight
     * @param teamPlayerTeam
     * @return
     */
    public TeamPlayer createTeamPlayer(String teamPlayerFirstname, String teamPlayerSurname, int teamPlayerHeight, int teamPlayerWeight, Team teamPlayerTeam)
    {
        TeamPlayer teamPlayer = teamPlayerService.createTeamPlayer(
            teamPlayerFirstname,
            teamPlayerSurname,
            teamPlayerHeight,
            teamPlayerWeight,
            teamPlayerTeam
        );

        entityManager.persist(teamPlayer);
        entityManager.flush();

        return teamPlayer;
    }

    /**
     * This method deletes a player.
     *
     * @param teamPlayerId
     */
    public void deleteTeamPlayer(UUID teamPlayerId)
    {
        TeamPlayer teamPlayer = teamPlayerRepository.getTeamPlayerById(teamPlayerId);

        entityManager
            .createQuery("DELETE FROM TeamMatchGoal tmg WHERE tmg.scorer.id = :tpid OR tmg.assistant.id = :tpid")
            .setParameter("tpid", teamPlayer.getId())
            .executeUpdate();

        entityManager.remove(teamPlayer);
        entityManager.flush();
    }

    /**
     * This method changes player's firstname.
     *
     * @param teamPlayerId
     * @param newTeamPlayerFirstname
     */
    public void changeTeamPlayerFirstname(UUID teamPlayerId, String newTeamPlayerFirstname)
    {
        TeamPlayer teamPlayer = teamPlayerRepository.getTeamPlayerById(teamPlayerId);

        teamPlayerService.changeTeamPlayerFirstname(teamPlayer, newTeamPlayerFirstname);

        entityManager.flush();
    }

    /**
     * This method changes player's firstname.
     *
     * @param teamPlayerId
     * @param newTeamPlayerSurname
     */
    public void changeTeamPlayerSurname(UUID teamPlayerId, String newTeamPlayerSurname)
    {
        TeamPlayer teamPlayer = teamPlayerRepository.getTeamPlayerById(teamPlayerId);

        teamPlayerService.changeTeamPlayerSurname(teamPlayer, newTeamPlayerSurname);

        entityManager.flush();
    }

    /**
     * This method changes player's firstname.
     *
     * @param teamPlayerId
     * @param newTeamPlayerHeight
     */
    public void changeTeamPlayerHeight(UUID teamPlayerId, int newTeamPlayerHeight)
    {
        TeamPlayer teamPlayer = teamPlayerRepository.getTeamPlayerById(teamPlayerId);

        teamPlayerService.changeTeamPlayerHeight(teamPlayer, newTeamPlayerHeight);

        entityManager.flush();
    }

    /**
     * This method changes player's firstname.
     *
     * @param teamPlayerId
     * @param newTeamPlayerWeight
     */
    public void changeTeamPlayerWeight(UUID teamPlayerId, int newTeamPlayerWeight)
    {
        TeamPlayer teamPlayer = teamPlayerRepository.getTeamPlayerById(teamPlayerId);

        teamPlayerService.changeTeamPlayerWeight(teamPlayer, newTeamPlayerWeight);

        entityManager.flush();
    }

    /**
     * This method changes player's firstname.
     *
     * @param teamPlayerId
     * @param newTeamPlayerTeam
     */
    public void changeTeamPlayerTeam(UUID teamPlayerId, Team newTeamPlayerTeam)
    {
        TeamPlayer teamPlayer = teamPlayerRepository.getTeamPlayerById(teamPlayerId);

        teamPlayerService.changeTeamPlayerTeam(teamPlayer, newTeamPlayerTeam);

        entityManager.flush();
    }
}
