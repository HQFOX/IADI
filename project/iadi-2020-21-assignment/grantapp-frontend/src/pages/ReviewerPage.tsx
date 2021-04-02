import React, {useEffect, useState} from "react";
import {Container} from "react-bootstrap";
import ApplicationDetails from "../components/ApplicationDetails";
import {Application} from "../types/Application.type";
import { getData} from "../Api";
import EvaluationPanelList from "../components/EvaluationPanelList";
import ApplicationList from "../components/ApplicationList";
import {DataItem} from "../types/DataItem.type";
import {AppDataItem} from "../types/AppDataItem.type";
import {Student} from "../types/Student.type";
import {Review} from "../types/Review.type";
import {EvaluationPanel} from "../types/EvaluationPanel.type";
import {useSelector} from "react-redux";
import {RootStore} from "../Store";
import '../css/table.css';



const defaultStudent: Student = {
    id: 0,
    name: "",
    birthday: "",
    email: "",
    address: ""
}


const defaultApplication : Application = {
    applicationId : 0,
    introduction : "",
    relatedWork : "",
    workPlan : "",
    publications : "",
    status : ""
}


function loadEvaluationPanels(setPanels:(panels:EvaluationPanel[]) => void, currentId:number) {
    getData('/reviewer/'+ currentId + '/panels',[])
        .then(data => {
            data && setPanels(data as EvaluationPanel[] )
        })
}

function loadApplications(setApplications:(applications:Application[]) => void, grantCallID: string) {
    getData('/grantcall/'+grantCallID+'/applications',[])
        .then(data => { data && setApplications(data as Application[] )})
}

function loadGrantCallDataItems(setDataItems:(items:DataItem[]) => void, grantCallID: string)  {
    getData('/grantcall/'+grantCallID+'/dataitems',[])
        .then(data => { data && setDataItems(data as DataItem[] )})
}
function loadAppDataItems(setAppdataItems:(appDataItems:AppDataItem[]) => void, applicationId:string) {
    getData('/application/' + applicationId + '/dataitems',[])
        .then(data => { data && setAppdataItems(data as AppDataItem[] )})
}

function loadStudent(setStudent:(student:Student) => void, applicationId: string)  {
    getData('/application/'+applicationId+'/student',[])
        .then(data => { data && setStudent(data as unknown as Student )})
}

function loadReviews(setReviews:(reviews:Review[]) => void, applicationId: string)  {
    getData('/review/app/'+applicationId,[])
        .then(data => { data && setReviews(data as Review[] ) })
}

const ReviewerPage = () => {

    //const [reviewer , setReviewer] = useState<ReviewerPanels>(defaultReviewer)
    const [panels , setPanels] = useState([] as EvaluationPanel[])
   // const [grants , setGrants] = useState([] as GrantCall[])
    const [selectedEvaluationPanelID , setSelectedEvaluationPanelID] = useState(0)
    const [dataItems, setDataItems] = useState([] as DataItem[])
    const [appdataItems, setAppDataItems] = useState([] as AppDataItem[])
    const [applications , setApplications] = useState([] as Application[])
    const [application , setApplication] = useState<Application>(defaultApplication)
    const [student , setStudent] = useState<Student>(defaultStudent)
    const [reviews , setReviews] = useState([] as Review[])

    const login = useSelector((state: RootStore) => state.login);


    useEffect( () => {
        //loadReviewer(setReviewer)
        //loadGrants(setGrants)
        loadEvaluationPanels(setPanels, login.currentId)
    } , [])


    function handleGrantClick(grantid:number, evaluationPanelID: number){
        clearData()
        loadApplications(setApplications, grantid.toString())
        loadGrantCallDataItems(setDataItems, grantid.toString())
        setSelectedEvaluationPanelID(evaluationPanelID)

    }

    function clearData(){
        setDataItems([])
        setAppDataItems([])
        setApplication(defaultApplication)
        setStudent(defaultStudent)
        setReviews([])
    }

    function handleApplicationClick(id:number){
        console.log("application id" + id)
        const app: Application[] = applications.filter( (app:Application) => app.applicationId === id)
        setApplication(app[0])
        loadAppDataItems(setAppDataItems, id.toString())
        loadStudent(setStudent, id.toString())
        loadReviews(setReviews, id.toString())
    }

    return (
        <div className="innerBox">
            <EvaluationPanelList panelList={panels} handleClick={handleGrantClick} />
            <hr className="hr-z"/>
            <ApplicationList applicationList={applications} handleClick={handleApplicationClick} />
            <hr className="hr-z"/>
            <ApplicationDetails
                application={application}
                dataItems={dataItems}
                appdataItems={appdataItems}
                student={student}
                reviews={reviews}
                evaluationPanelId={selectedEvaluationPanelID}
                role={"reviewer"}
            />

        </div>
    )
}

export default ReviewerPage