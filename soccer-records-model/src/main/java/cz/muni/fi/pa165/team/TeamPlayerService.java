package cz.muni.fi.pa165.team;

/**
 * @author Libor Mühlpachr <libor.muhl@seznam.cz>
 */
public class TeamPlayerService
{

    public TeamPlayer createTeamPlayer(String firstname, String surname, int height, int weight, Team team)
    {
        return new TeamPlayer(firstname, surname, height, weight, team);
    }

    public void changeTeamPlayerFirstname(TeamPlayer tp, String firstname)
    {
        tp.changeFirstname(firstname);
    }

    public void changeTeamPlayerSurname(TeamPlayer tp, String surname)
    {
        tp.changeSurname(surname);
    }

    public void changeTeamPlayerHeight(TeamPlayer tp, int height)
    {
        tp.changeHeight(height);
    }

    public void changeTeamPlayerWeight(TeamPlayer tp, int weight)
    {
        tp.changeWeight(weight);
    }

    public void changeTeamPlayerTeam(TeamPlayer tp, Team team)
    {
        tp.changeTeam(team);
    }
}
