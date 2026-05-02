// src/app/features/admin/components/english-kids/admin-english-kids.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EnglishKidsCourseService } from '../../../../core/services/english-kids-course.service';
import { ParentService } from '../../../../core/services/parent.service';
import { ChildService } from '../../../../core/services/child.service';
import { ProgressService } from '../../../../core/services/progress.service';
import { RewardService } from '../../../../core/services/reward.service';
import { ActivityService } from '../../../../core/services/activity.service';
import { QuestionService } from '../../../../core/services/question.service';
import { AnalyticsService } from '../../../../core/services/analytics.service';
import { Course } from '../../../../core/models/course.model';
import { Parent } from '../../../../core/models/parent.model';
import { Child } from '../../../../core/models/child.model';
import { Progress } from '../../../../core/models/progress.model';
import { Reward, TypeReward } from '../../../../core/models/reward.model';
import { Activity, ActivityType } from '../../../../core/models/english-kids-activity.model';
import { Question, QuizOption } from '../../../../core/models/english-kids-question.model';
import { AnalyticsData } from '../../../../core/models/analytics.model';

@Component({
    selector: 'app-admin-english-kids',
    templateUrl: './admin-english-kids.component.html',
    styleUrls: ['./admin-english-kids.component.css']
})
export class AdminEnglishKidsComponent implements OnInit {
    // Onglet actif
    activeTab: 'courses' | 'parents' | 'children' | 'progress' | 'rewards' | 'activities' | 'analytics' = 'courses';
    
    // Données
    courses: Course[] = [];
    parents: Parent[] = [];
    children: Child[] = [];
    progressList: Progress[] = [];
    rewards: Reward[] = [];
    activities: any[] = [];  // Activities list
    analyticsData: AnalyticsData | null = null;
    
    // Données filtrées
    filteredCourses: Course[] = [];
    filteredParents: Parent[] = [];
    filteredChildren: Child[] = [];
    filteredProgressList: Progress[] = [];
    filteredRewards: Reward[] = [];
    filteredActivities: any[] = [];
    
    // Recherche
    searchTerm: string = '';
    
    loading = false;
    showModal = false;
    isEditMode = false;
    saving = false;
    
    // Quiz Editor
    showQuizEditor = false;
    currentActivity: any = null;
    questions: Question[] = [];
    currentQuestion: Question = this.createEmptyQuestion();
    
    // Forms
    courseForm: FormGroup;
    parentForm: FormGroup;
    childForm: FormGroup;
    progressForm: FormGroup;
    rewardForm: FormGroup;
    activityForm: FormGroup;
    
    currentCourseId?: number;
    currentParentId?: number;
    currentChildId?: number;
    currentProgressId?: number;
    currentRewardId?: number;
    currentActivityId?: number;
    
    // Enum pour le template
    typeRewardEnum = TypeReward;
    activityTypeEnum = ActivityType;

    constructor(
        private courseService: EnglishKidsCourseService,
        private parentService: ParentService,
        private childService: ChildService,
        private progressService: ProgressService,
        private rewardService: RewardService,
        private activityService: ActivityService,
        private questionService: QuestionService,
        private analyticsService: AnalyticsService,
        private fb: FormBuilder
    ) {
        this.courseForm = this.fb.group({
            title: ['', Validators.required],
            description: [''],
            price: [0, [Validators.required, Validators.min(0)]]
        });
        
        this.parentForm = this.fb.group({
            phone: ['', Validators.required],
            address: ['', Validators.required],
            userId: ['', Validators.required]
        });
        
        this.childForm = this.fb.group({
            name: ['', Validators.required],
            birthDate: ['', Validators.required]
        });
        
        this.progressForm = this.fb.group({
            completionRate: [0, [Validators.required, Validators.min(0), Validators.max(100)]]
        });
        
        this.rewardForm = this.fb.group({
            name: ['', Validators.required],
            type: ['BADGES', Validators.required],
            pointsRequired: [0, [Validators.required, Validators.min(0)]]
        });
        
        this.activityForm = this.fb.group({
            title: ['', Validators.required],
            type: ['QUIZ', Validators.required],
            contentUrl: [''],
            points: [10, [Validators.required, Validators.min(0)]],
            orderIndex: [1, [Validators.required, Validators.min(1)]],
            course: [null, Validators.required]
        });
    }

    ngOnInit(): void {
        this.loadData();
    }
    
    switchTab(tab: 'courses' | 'parents' | 'children' | 'progress' | 'rewards' | 'activities' | 'analytics'): void {
        this.activeTab = tab;
        this.loadData();
    }
    
    loadData(): void {
        switch(this.activeTab) {
            case 'courses':
                this.loadCourses();
                break;
            case 'parents':
                this.loadParents();
                break;
            case 'children':
                this.loadChildren();
                break;
            case 'progress':
                this.loadProgress();
                break;
            case 'rewards':
                this.loadRewards();
                break;
            case 'activities':
                this.loadActivities();
                break;
            case 'analytics':
                this.loadAnalytics();
                break;
        }
    }

    
    // LOAD METHODS
    loadCourses(): void {
        this.loading = true;
        this.courseService.getAllCourses().subscribe({
            next: (data) => {
                this.courses = data;
                this.filteredCourses = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading courses:', error);
                this.loading = false;
                alert('Error loading courses');
            }
        });
    }
    
    loadParents(): void {
        this.loading = true;
        this.parentService.getAllParents().subscribe({
            next: (data) => {
                this.parents = data;
                this.filteredParents = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading parents:', error);
                this.loading = false;
                alert('Error loading parents');
            }
        });
    }
    
    loadChildren(): void {
        this.loading = true;
        this.childService.getAllChildren().subscribe({
            next: (data) => {
                this.children = data;
                this.filteredChildren = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading children:', error);
                this.loading = false;
                alert('Error loading children');
            }
        });
    }
    
    loadProgress(): void {
        this.loading = true;
        this.progressService.getAllProgress().subscribe({
            next: (data) => {
                this.progressList = data;
                this.filteredProgressList = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading progress:', error);
                this.loading = false;
                alert('Error loading progress');
            }
        });
    }
    
    loadRewards(): void {
        this.loading = true;
        this.rewardService.getAllRewards().subscribe({
            next: (data) => {
                this.rewards = data;
                this.filteredRewards = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading rewards:', error);
                this.loading = false;
                alert('Error loading rewards');
            }
        });
    }
    
    loadActivities(): void {
        this.loading = true;
        this.activityService.getAllActivities().subscribe({
            next: (data) => {
                this.activities = data;
                this.filteredActivities = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading activities:', error);
                this.loading = false;
                alert('Error loading activities');
            }
        });
    }
    
    loadAnalytics(): void {
        this.loading = true;
        const endDate = new Date().toISOString().split('T')[0];
        const startDate = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];
        
        this.analyticsService.getAnalytics(startDate, endDate).subscribe({
            next: (data) => {
                this.analyticsData = data;
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading analytics:', error);
                this.loading = false;
                alert('Error loading analytics');
            }
        });
    }
    
    // RECHERCHE
    onSearch(): void {
        const term = this.searchTerm.toLowerCase();
        
        switch(this.activeTab) {
            case 'courses':
                this.filteredCourses = this.courses.filter(c => 
                    c.title.toLowerCase().includes(term) || 
                    (c.description && c.description.toLowerCase().includes(term))
                );
                break;
            case 'parents':
                this.filteredParents = this.parents.filter(p => 
                    p.phone.toLowerCase().includes(term) || 
                    p.address.toLowerCase().includes(term)
                );
                break;
            case 'children':
                this.filteredChildren = this.children.filter(c => 
                    c.name.toLowerCase().includes(term)
                );
                break;
            case 'progress':
                this.filteredProgressList = this.progressList;
                break;
            case 'rewards':
                this.filteredRewards = this.rewards.filter(r => 
                    r.name.toLowerCase().includes(term) || 
                    r.type.toLowerCase().includes(term)
                );
                break;
            case 'activities':
                this.filteredActivities = this.activities.filter(a => 
                    a.title.toLowerCase().includes(term) || 
                    a.type.toLowerCase().includes(term)
                );
                break;
        }
    }
    
    // TRI
    sortBy(field: string): void {
        switch(this.activeTab) {
            case 'courses':
                this.filteredCourses.sort((a: any, b: any) => a[field] > b[field] ? 1 : -1);
                break;
            case 'parents':
                this.filteredParents.sort((a: any, b: any) => a[field] > b[field] ? 1 : -1);
                break;
            case 'children':
                this.filteredChildren.sort((a: any, b: any) => a[field] > b[field] ? 1 : -1);
                break;
            case 'progress':
                this.filteredProgressList.sort((a: any, b: any) => a[field] > b[field] ? 1 : -1);
                break;
            case 'rewards':
                this.filteredRewards.sort((a: any, b: any) => a[field] > b[field] ? 1 : -1);
                break;
            case 'activities':
                this.filteredActivities.sort((a: any, b: any) => a[field] > b[field] ? 1 : -1);
                break;
        }
    }
    
    // MODAL MANAGEMENT
    openAddModal(): void {
        this.isEditMode = false;
        switch(this.activeTab) {
            case 'courses':
                this.courseForm.reset({ title: '', description: '', price: 0 });
                break;
            case 'parents':
                this.parentForm.reset({ phone: '', address: '', userId: '' });
                break;
            case 'children':
                this.childForm.reset({ name: '', birthDate: '' });
                break;
            case 'progress':
                this.progressForm.reset({ completionRate: 0 });
                break;
            case 'rewards':
                this.rewardForm.reset({ name: '', type: 'BADGES', pointsRequired: 0 });
                break;
            case 'activities':
                this.activityForm.reset({ title: '', type: 'QUIZ', contentUrl: '', points: 10, orderIndex: 1, course: null });
                break;
        }
        this.showModal = true;
    }

    openEditModal(item: any): void {
        this.isEditMode = true;
        switch(this.activeTab) {
            case 'courses':
                this.currentCourseId = item.courseId;
                this.courseForm.patchValue(item);
                break;
            case 'parents':
                this.currentParentId = item.parentId;
                this.parentForm.patchValue(item);
                break;
            case 'children':
                this.currentChildId = item.childId;
                this.childForm.patchValue(item);
                break;
            case 'progress':
                this.currentProgressId = item.progressId;
                this.progressForm.patchValue(item);
                break;
            case 'rewards':
                this.currentRewardId = item.rewardId;
                this.rewardForm.patchValue(item);
                break;
            case 'activities':
                this.currentActivityId = item.activityId;
                this.activityForm.patchValue(item);
                break;
        }
        this.showModal = true;
    }

    
    // DELETE METHODS
    deleteCourse(id: number): void {
        if (confirm('Are you sure you want to delete this course?')) {
            this.courseService.deleteCourse(id).subscribe({
                next: () => {
                    this.loadCourses();
                    alert('Course deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting course:', error);
                    alert('Error deleting course');
                }
            });
        }
    }
    
    deleteParent(id: number): void {
        if (confirm('Are you sure you want to delete this parent?')) {
            this.parentService.deleteParent(id).subscribe({
                next: () => {
                    this.loadParents();
                    alert('Parent deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting parent:', error);
                    alert('Error deleting parent');
                }
            });
        }
    }
    
    deleteChild(id: number): void {
        if (confirm('Are you sure you want to delete this child?')) {
            this.childService.deleteChild(id).subscribe({
                next: () => {
                    this.loadChildren();
                    alert('Child deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting child:', error);
                    alert('Error deleting child');
                }
            });
        }
    }
    
    deleteProgress(id: number): void {
        if (confirm('Are you sure you want to delete this progress?')) {
            this.progressService.deleteProgress(id).subscribe({
                next: () => {
                    this.loadProgress();
                    alert('Progress deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting progress:', error);
                    alert('Error deleting progress');
                }
            });
        }
    }
    
    deleteReward(id: number): void {
        if (confirm('Are you sure you want to delete this reward?')) {
            this.rewardService.deleteReward(id).subscribe({
                next: () => {
                    this.loadRewards();
                    alert('Reward deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting reward:', error);
                    alert('Error deleting reward');
                }
            });
        }
    }
    
    deleteActivity(id: number): void {
        if (confirm('Are you sure you want to delete this activity?')) {
            this.activityService.deleteActivity(id).subscribe({
                next: () => {
                    this.loadActivities();
                    alert('Activity deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting activity:', error);
                    alert('Error deleting activity');
                }
            });
        }
    }

    
    // SUBMIT METHODS
    onSubmit(): void {
        switch(this.activeTab) {
            case 'courses':
                this.submitCourse();
                break;
            case 'parents':
                this.submitParent();
                break;
            case 'children':
                this.submitChild();
                break;
            case 'progress':
                this.submitProgress();
                break;
            case 'rewards':
                this.submitReward();
                break;
            case 'activities':
                this.submitActivity();
                break;
        }
    }
    
    submitCourse(): void {
        if (this.courseForm.valid) {
            this.saving = true;
            const course: Course = this.courseForm.value;

            if (this.isEditMode && this.currentCourseId) {
                course.courseId = this.currentCourseId;
                this.courseService.updateCourse(course).subscribe({
                    next: () => {
                        this.loadCourses();
                        this.closeModal();
                        this.saving = false;
                        alert('Course updated successfully!');
                    },
                    error: (error) => {
                        console.error('Error updating course:', error);
                        this.saving = false;
                        alert('Error updating course');
                    }
                });
            } else {
                this.courseService.addCourse(course).subscribe({
                    next: () => {
                        this.loadCourses();
                        this.closeModal();
                        this.saving = false;
                        alert('Course added successfully!');
                    },
                    error: (error) => {
                        console.error('Error adding course:', error);
                        this.saving = false;
                        alert('Error adding course');
                    }
                });
            }
        }
    }
    
    submitParent(): void {
        if (this.parentForm.valid) {
            this.saving = true;
            const parent: Parent = this.parentForm.value;

            if (this.isEditMode && this.currentParentId) {
                parent.parentId = this.currentParentId;
                this.parentService.updateParent(parent).subscribe({
                    next: () => {
                        this.loadParents();
                        this.closeModal();
                        this.saving = false;
                        alert('Parent updated successfully!');
                    },
                    error: (error) => {
                        console.error('Error updating parent:', error);
                        this.saving = false;
                        alert('Error updating parent');
                    }
                });
            } else {
                this.parentService.addParent(parent).subscribe({
                    next: () => {
                        this.loadParents();
                        this.closeModal();
                        this.saving = false;
                        alert('Parent added successfully!');
                    },
                    error: (error) => {
                        console.error('Error adding parent:', error);
                        this.saving = false;
                        alert('Error adding parent');
                    }
                });
            }
        }
    }
    
    submitChild(): void {
        if (this.childForm.valid) {
            this.saving = true;
            const child: Child = this.childForm.value;

            if (this.isEditMode && this.currentChildId) {
                child.childId = this.currentChildId;
                this.childService.updateChild(child).subscribe({
                    next: () => {
                        this.loadChildren();
                        this.closeModal();
                        this.saving = false;
                        alert('Child updated successfully!');
                    },
                    error: (error) => {
                        console.error('Error updating child:', error);
                        this.saving = false;
                        alert('Error updating child');
                    }
                });
            } else {
                this.childService.addChild(child).subscribe({
                    next: () => {
                        this.loadChildren();
                        this.closeModal();
                        this.saving = false;
                        alert('Child added successfully!');
                    },
                    error: (error) => {
                        console.error('Error adding child:', error);
                        this.saving = false;
                        alert('Error adding child');
                    }
                });
            }
        }
    }
    
    submitProgress(): void {
        if (this.progressForm.valid) {
            this.saving = true;
            const progress: Progress = this.progressForm.value;

            if (this.isEditMode && this.currentProgressId) {
                progress.progressId = this.currentProgressId;
                this.progressService.updateProgress(progress).subscribe({
                    next: () => {
                        this.loadProgress();
                        this.closeModal();
                        this.saving = false;
                        alert('Progress updated successfully!');
                    },
                    error: (error) => {
                        console.error('Error updating progress:', error);
                        this.saving = false;
                        alert('Error updating progress');
                    }
                });
            } else {
                this.progressService.addProgress(progress).subscribe({
                    next: () => {
                        this.loadProgress();
                        this.closeModal();
                        this.saving = false;
                        alert('Progress added successfully!');
                    },
                    error: (error) => {
                        console.error('Error adding progress:', error);
                        this.saving = false;
                        alert('Error adding progress');
                    }
                });
            }
        }
    }
    
    submitReward(): void {
        if (this.rewardForm.valid) {
            this.saving = true;
            const reward: Reward = this.rewardForm.value;

            if (this.isEditMode && this.currentRewardId) {
                reward.rewardId = this.currentRewardId;
                this.rewardService.updateReward(reward).subscribe({
                    next: () => {
                        this.loadRewards();
                        this.closeModal();
                        this.saving = false;
                        alert('Reward updated successfully!');
                    },
                    error: (error) => {
                        console.error('Error updating reward:', error);
                        this.saving = false;
                        alert('Error updating reward');
                    }
                });
            } else {
                this.rewardService.addReward(reward).subscribe({
                    next: () => {
                        this.loadRewards();
                        this.closeModal();
                        this.saving = false;
                        alert('Reward added successfully!');
                    },
                    error: (error) => {
                        console.error('Error adding reward:', error);
                        this.saving = false;
                        alert('Error adding reward');
                    }
                });
            }
        }
    }
    
    submitActivity(): void {
        if (this.activityForm.valid) {
            this.saving = true;
            const activity: Activity = this.activityForm.value;

            if (this.isEditMode && this.currentActivityId) {
                activity.activityId = this.currentActivityId;
                this.activityService.updateActivity(activity).subscribe({
                    next: () => {
                        this.loadActivities();
                        this.closeModal();
                        this.saving = false;
                        alert('Activity updated successfully!');
                    },
                    error: (error) => {
                        console.error('Error updating activity:', error);
                        this.saving = false;
                        alert('Error updating activity');
                    }
                });
            } else {
                this.activityService.addActivity(activity).subscribe({
                    next: () => {
                        this.loadActivities();
                        this.closeModal();
                        this.saving = false;
                        alert('Activity added successfully!');
                    },
                    error: (error) => {
                        console.error('Error adding activity:', error);
                        this.saving = false;
                        alert('Error adding activity');
                    }
                });
            }
        }
    }
    
    closeModal(): void {
        this.showModal = false;
        this.courseForm.reset();
        this.parentForm.reset();
        this.childForm.reset();
        this.progressForm.reset();
        this.rewardForm.reset();
        this.activityForm.reset();
        this.currentCourseId = undefined;
        this.currentParentId = undefined;
        this.currentChildId = undefined;
        this.currentProgressId = undefined;
        this.currentRewardId = undefined;
        this.currentActivityId = undefined;
    }
    
    getCurrentForm(): FormGroup {
        switch(this.activeTab) {
            case 'courses': return this.courseForm;
            case 'parents': return this.parentForm;
            case 'children': return this.childForm;
            case 'progress': return this.progressForm;
            case 'rewards': return this.rewardForm;
            case 'activities': return this.activityForm;
            default: return this.courseForm;
        }
    }
    
    // QUIZ EDITOR METHODS
    openQuizEditor(activity: any): void {
        this.currentActivity = activity;
        this.showQuizEditor = true;
        this.loadQuestions(activity.activityId);
    }
    
    loadQuestions(activityId: number): void {
        this.questionService.getActivityWithQuestions(activityId).subscribe({
            next: (data) => {
                this.questions = data.questions || [];
            },
            error: (error) => {
                console.error('Error loading questions:', error);
                this.questions = [];
            }
        });
    }
    
    addQuestion(): void {
        if (!this.currentActivity) return;
        
        this.questionService.addQuestion(this.currentActivity.activityId, this.currentQuestion).subscribe({
            next: () => {
                this.loadQuestions(this.currentActivity.activityId);
                this.currentQuestion = this.createEmptyQuestion();
                alert('Question added successfully!');
            },
            error: (error) => {
                console.error('Error adding question:', error);
                alert('Error adding question');
            }
        });
    }
    
    deleteQuestion(questionId: number): void {
        if (confirm('Are you sure you want to delete this question?')) {
            this.questionService.deleteQuestion(questionId).subscribe({
                next: () => {
                    this.loadQuestions(this.currentActivity.activityId);
                    alert('Question deleted successfully!');
                },
                error: (error) => {
                    console.error('Error deleting question:', error);
                    alert('Error deleting question');
                }
            });
        }
    }
    
    createEmptyQuestion(): Question {
        return {
            questionText: '',
            imageUrl: '',
            correctAnswerIndex: 0,
            orderIndex: this.questions.length + 1,
            options: [
                { optionText: '', imageUrl: '', orderIndex: 0 },
                { optionText: '', imageUrl: '', orderIndex: 1 },
                { optionText: '', imageUrl: '', orderIndex: 2 },
                { optionText: '', imageUrl: '', orderIndex: 3 }
            ]
        };
    }
    
    closeQuizEditor(): void {
        this.showQuizEditor = false;
        this.currentActivity = null;
        this.questions = [];
        this.currentQuestion = this.createEmptyQuestion();
    }
    
    isQuestionFormValid(): boolean {
        if (!this.currentQuestion.questionText) return false;
        return this.currentQuestion.options.some(o => o.optionText && o.optionText.trim() !== '');
    }
}
