export enum TypeReward {
  BADGES = 'BADGES',
  STARS = 'STARS',
  GIFTS = 'GIFTS'
}

export interface Reward {
  rewardId?: number;
  name: string;
  type: TypeReward;
  pointsRequired: number;
}
