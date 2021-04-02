import * as React from 'react';
import {useEffect, useState} from 'react';
import "../css/routerpage.css"
import {Accordion, Card, Form, Modal} from 'react-bootstrap';
import {MdDescription, MdRateReview, MdTitle} from "react-icons/md";
import {
    AiOutlineMail,
    AiOutlineSchedule,
    BiCheckSquare,
    GrStatusInfo,
    ImAttachment,
    IoIosGitNetwork,
    RiFilePaper2Line,
    VscDebugStart
} from "react-icons/all";

export interface Props {
    show: boolean;
    handleClose: () => void;
    app: any
}

interface DataItem {
    data: String
}

function AppDetails({app, show, handleClose}: Props) {
    useEffect(() => {
        fetchGrantDataItem();
        fetchData();
        fetchReviews();
    }, []);

    const [appDataItems, setAppDataItems] = useState([]);
    const [grantDataItems, setGrantDataItems] = useState([]);
    const [reviews, setReviews] = useState([]);

    const getItemResponse = (grantDataItemId: number) => {
        let find: any = appDataItems.find((a: any) => a.dataItemId === grantDataItemId);
        if (typeof find === 'undefined')
            return "No file submitted!";
        else
            return "File: " + find.data;
    }

    const fetchData = async () => {
        fetch('/application/' + app.applicationId + '/dataitems', {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(data => {
                setAppDataItems(data);
            })
    }

    const fetchGrantDataItem = async () => {
        fetch('/grantcall/' + app.grantId + '/dataitems', {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(data => {
                setGrantDataItems(data);
            })
    }

    const fetchReviews = async () => {
        fetch('/review/app/' + app.applicationId, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(data => {
                setReviews(data);
            })
    }

    return (
        <Modal centered size="lg" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Submitted Application</Modal.Title>
            </Modal.Header>
            <Form>
                <Modal.Body>
                    <div><MdTitle className="icon"/>{app.grantTitle}</div>
                    <div><GrStatusInfo className="icon"/>{app.status} </div>
                    <div><MdDescription className="icon"/>Description: <br/>
                        <textarea readOnly defaultValue={app.grantDescription}/></div>
                    <hr/>
                    <Accordion>
                        <Card>
                            <Accordion.Toggle as={Card.Header} eventKey="0">
                                <VscDebugStart className="icon no-border"/> Introduction
                            </Accordion.Toggle>
                            <Accordion.Collapse eventKey="0">
                                <Card.Body>{app.introduction}</Card.Body>
                            </Accordion.Collapse>
                        </Card>
                        <Card>
                            <Accordion.Toggle as={Card.Header} eventKey="1">
                                <AiOutlineSchedule className="icon no-border"/> Work Plan
                            </Accordion.Toggle>
                            <Accordion.Collapse eventKey="1">
                                <Card.Body>{app.workPlan}</Card.Body>
                            </Accordion.Collapse>
                        </Card>
                        <Card>
                            <Accordion.Toggle as={Card.Header} eventKey="2">
                                <IoIosGitNetwork className="icon no-border"/> Related Work
                            </Accordion.Toggle>
                            <Accordion.Collapse eventKey="2">
                                <Card.Body>{app.relatedWork}</Card.Body>
                            </Accordion.Collapse>
                        </Card>
                        <Card>
                            <Accordion.Toggle as={Card.Header} eventKey="3">
                                <RiFilePaper2Line className="icon no-border"/> Publications
                            </Accordion.Toggle>
                            <Accordion.Collapse eventKey="3">
                                <Card.Body>{app.publications}</Card.Body>
                            </Accordion.Collapse>
                        </Card>
                        {grantDataItems.map((item: any) => (
                            <Card>
                                <Accordion.Toggle as={Card.Header} eventKey={'di' + item.dataItemId}>
                                    <ImAttachment
                                        className="icon no-border"/> {item.dataType} {item.mandatory == true ? (
                                    <div className="dataitem-mandatory">Mandatory Item <BiCheckSquare/>
                                    </div>) : (null)}
                                </Accordion.Toggle>
                                <Accordion.Collapse eventKey={'di' + item.dataItemId}>
                                    <Card.Body>
                                        {getItemResponse(item.dataItemId)}
                                    </Card.Body>
                                </Accordion.Collapse>
                            </Card>
                        ))}
                    </Accordion>
                    <hr/>
                    <Accordion>
                        {reviews.map((item: any) => (
                            <Card>
                                <Accordion.Toggle as={Card.Header} eventKey={'r' + item.reviewId}>
                                    <MdRateReview
                                        className="icon no-border"/> {item.reviewer.username}
                                    <div className="dataitem-mandatory">{item.reviewer.email} <AiOutlineMail/></div>
                                </Accordion.Toggle>
                                <Accordion.Collapse eventKey={'r' + item.reviewId}>
                                    <Card.Body>
                                        {item.writtenReview}
                                    </Card.Body>
                                </Accordion.Collapse>
                            </Card>
                        ))}
                    </Accordion>
                </Modal.Body>
            </Form>
        </Modal>
    )
}

export default AppDetails;