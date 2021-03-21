import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { generateTeam } from '../mock-teams';
import { Team } from '../team';
import { TeamService } from '../team.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-team-editor',
  templateUrl: './team-editor.component.html',
  styleUrls: ['./team-editor.component.css'],
})
export class TeamEditorComponent implements OnInit {
  teams: Team[] = [];

  constructor(private teamService: TeamService) {}

  ngOnInit(): void {
    this.teamService.getTeams(environment.tournamentId).subscribe(
      (teams) => (this.teams = teams),
      (error) => console.log(error)
    );
  }

  addTeam(): void {
    const newTeam = generateTeam(this.genId());
    this.teams.push(newTeam);
  }

  deleteTeam(team: Team): void {
    this.teams = this.teams.filter(t => t != team);
  }

  genId(): string {
    return uuidv4();
  }
}
