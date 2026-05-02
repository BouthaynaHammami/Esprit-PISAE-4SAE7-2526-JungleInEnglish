import { Component, OnInit } from '@angular/core';
import { StudentChallenge, Badge } from '../../../../../core/models/challenges-competitions.model';
import { StudentChallengeService } from '../../../../../core/services/activity/student-challenge.service';
import { BadgeService } from '../../../../../core/services/activity/badge.service';

interface StudentGroup {
  idUser: number;
  challenges: StudentChallenge[];
  totalScore: number;
  badges: Badge[];
}

@Component({
  selector: 'app-admin-student-stats',
  templateUrl: './admin-student-stats.component.html',
  styleUrls: ['./admin-student-stats.component.scss']
})
export class AdminStudentStatsComponent implements OnInit {

  loading = false;
  error: string | null = null;

  studentGroups: StudentGroup[] = [];
  filteredGroups: StudentGroup[] = [];

  nameQuery = '';
  expanded = new Set<number>();

  constructor(
    private studentChallengeService: StudentChallengeService,
    private badgeService: BadgeService
  ) { }

  ngOnInit(): void {
    this.loadAllData();
  }

  loadAllData(): void {
    this.loading = true;
    this.error = null;

    this.studentChallengeService.getAll().subscribe({
      next: (challenges) => {
        this.processChallenges(challenges);
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load student statistics.';
        this.loading = false;
      }
    });
  }

  private processChallenges(challenges: StudentChallenge[]): void {
    const groupsMap = new Map<number, StudentChallenge[]>();
    
    challenges.forEach(c => {
      const list = groupsMap.get(c.idUser) || [];
      list.push(c);
      groupsMap.set(c.idUser, list);
    });

    this.studentGroups = Array.from(groupsMap.entries()).map(([idUser, userChallenges]) => {
      const totalScore = userChallenges.reduce((sum, c) => sum + (c.progress || 0), 0);
      return {
        idUser,
        challenges: userChallenges.sort((a, b) => {
          const dateA = new Date(a.startDate || 0).getTime();
          const dateB = new Date(b.startDate || 0).getTime();
          return dateB - dateA;
        }),
        totalScore,
        badges: [] 
      };
    }).sort((a, b) => b.totalScore - a.totalScore);

    this.applyFilter();
  }

  toggleDetails(idUser: number): void {
    if (this.expanded.has(idUser)) {
      this.expanded.delete(idUser);
    } else {
      this.expanded.add(idUser);
    }
  }

  isExpanded(idUser: number): boolean {
    return this.expanded.has(idUser);
  }

  applyFilter(): void {
    const q = this.nameQuery.trim().toLowerCase();
    if (!q) {
      this.filteredGroups = [...this.studentGroups];
      return;
    }
    this.filteredGroups = this.studentGroups.filter(g => 
      g.idUser.toString().includes(q)
    );
  }

  progressWidth(p?: number | null): string {
    const v = Math.max(0, Math.min(100, Number(p ?? 0)));
    return `${v}%`;
  }
}
