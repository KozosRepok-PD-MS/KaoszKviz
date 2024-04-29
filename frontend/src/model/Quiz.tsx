
export interface Quiz {
    id?: number;
    isPublic?: boolean;
    ownerId?: number;
    title?: string;
    description?: string;
    shortAccessibleName?: string;
    mediaContentOwnerId?: string;
    mediaContentName?: string;
}

export type TQuizList = Quiz[];

export interface QuizCreateFormType {
    ownerId?: bigint;
    title: string;
    description: string;
    isPublic: boolean;
    shortAccessibleName: string;
    mediaContentOwnerId: bigint;
    mediaContentName: string;
}

export interface QuizModifyFormType {
    id?: String; //!TODO nem string
    title: string;
    description: string;
    isPublic: boolean;
    shortAccessibleName: string;
    mediaContentOwnerId: bigint;
    mediaContentName: string;
}
