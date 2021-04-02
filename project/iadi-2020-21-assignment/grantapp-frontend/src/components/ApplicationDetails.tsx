import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row} from "react-bootstrap";
import {Application} from "../types/Application.type";
import {Student} from "../types/Student.type";
import StudentDetails from "./StudentDetails";
import ReviewsTable from "./ReviewsTable";
import {Review} from "../types/Review.type";
import DataItemsTable from "./DataItemsTable";

import {DataItem} from "../types/DataItem.type";

import {AppDataItem} from "../types/AppDataItem.type";
import CreateReviewModal from "../pages/modals/CreateReviewModal";
import CloseApplicationModal from "../pages/modals/CloseApplicationModal";


const ApplicationDetails = (props:{application:Application, dataItems:DataItem[], appdataItems:AppDataItem[], student:Student, reviews:Review[], evaluationPanelId:number , role:string}) => {



    const [show, setShow] = useState<boolean>(false);



    function handleShow(value:boolean) {
        setShow(value);
    }



    useEffect( () => {

    } , [])
    return (
        <div className="innerBox">
            { props.application.applicationId !== 0 ?
            <Row>

                <Col>
                    <Container fluid>
                        <Row>
                            <Col>
                                <h4>Introduction:</h4>
                                <h6>{props.application.introduction}</h6>
                            </Col>
                            <Col>
                                <h4>Related Work:</h4>
                                <h6>{props.application.relatedWork}</h6>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <h4>Work Plan:</h4>
                                <h6>{props.application.workPlan}</h6>
                            </Col>
                            <Col>
                                <h4>Publications:</h4>
                                <h6>{props.application.publications}</h6>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <h5>Status: {props.application.status}</h5>
                            </Col>
                            <Col>
                                { props.role === "reviewer" ?
                                    <Button onClick={ ()=>handleShow(true) }> Write a New Review</Button>
                                    :
                                    <Button onClick={ ()=>handleShow(true) }> Close Application </Button>
                                }

                            </Col>
                        </Row>
                    </Container>
                </Col>


                { props.reviews.length > 0 ?
                <Col>
                    <ReviewsTable reviews={props.reviews}/>
                </Col>
                : null }

                { props.dataItems.length > 0 ?
                <Col>
                    <DataItemsTable dataitems={props.dataItems} appdataitems={props.appdataItems}/>
                </Col>

                : null }
                <Col>
                    <StudentDetails student={props.student}/>
                </Col>
                {props.role === "reviewer" ?
                <CreateReviewModal applicationId={props.application.applicationId} evaluationPanelId={props.evaluationPanelId} show={show} handleShow={handleShow}  />
                : <CloseApplicationModal show={show} handleShow={handleShow} application={props.application} evaluationPanelId={props.evaluationPanelId} />
                }
            </Row>
                : null }
        </div>

    )
}

export default ApplicationDetails;