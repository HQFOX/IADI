import {Reviewer} from "./Reviewer.type";
import {Application} from "./Application.type";

export type Review = {
    reviewId:number,
    writtenReview:string
    application:Application
    reviewer:Reviewer

}

export type ReviewPost = {
    "reviewId": number,
    "writtenReview": string,
    "applicationId": number,
    "reviewerId": number,
    "evaluationPanelId": number

}
