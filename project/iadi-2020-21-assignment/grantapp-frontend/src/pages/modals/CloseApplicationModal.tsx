import React, {useEffect, useState} from "react";
import {Button, Form, Modal} from "react-bootstrap";
import {postData, putData} from "../../Api";
import {useSelector} from "react-redux";
import {RootStore} from "../../Store";
import {Application} from "../../types/Application.type";
import {ReviewPost} from "../../types/Review.type";


const initialReview:ReviewPost = {
    reviewId: 0,
    writtenReview: "",
    applicationId: 0,
    reviewerId: 0,
    evaluationPanelId: 0
}

const CloseApplicationModal = (props:{show:boolean, handleShow:(value:boolean) => void, application:Application, evaluationPanelId:number}) => {

    const [application, setApplication] = useState<Application>(props.application)
    const [review, setReview] = useState<ReviewPost>(initialReview)

    const login = useSelector((state: RootStore) => state.login);

    useEffect( () => {
        setReview( {
            ...review,
            evaluationPanelId: props.evaluationPanelId,
            applicationId: props.application.applicationId,
            reviewerId: login.currentId
        })
    } , [])

    function handleChangeText(event: { currentTarget: { name: any; value: any; }; })
    {
        const { name , value } = event.currentTarget;

        setReview({
            ...review,
            [name] : value
        })
    }

    function handleChangeDropDown( event: string )
    {
        setApplication({
            ...application,
            status : event
        })
    }

    function handleSubmit() {
        console.log(application)
        console.log(review)
        if(review.writtenReview !== "")
            postData("review",review)
        putData('application/' + application.applicationId + '/status',application).then(
            () => props.handleShow(false)
        )
    }

    return (
        <Modal show={props.show} onHide={() => props.handleShow(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Close Application</Modal.Title>
            </Modal.Header>
            <Modal.Body>

                <Form.Group>
                    <Form.Label><h5>Review:</h5></Form.Label>
                    <Form.Control name="writtenReview" onChange={handleChangeText} as="textarea" rows={3} value={review.writtenReview} />
                </Form.Group>

                <Form.Group  >
                    <Form.Label>Change Application Status</Form.Label>
                    <Form.Control as="select" onChange={(event:any ) => handleChangeDropDown(event.target.value)} value={application.status}>
                        <option value="Waiting Response" >Waiting Response</option>
                        <option value="Accepted">Accepted</option>
                        <option value="Denied">Denied</option>

                    </Form.Control>
                </Form.Group>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => props.handleShow(false)}>
                    Close
                </Button>
                <Button onClick={handleSubmit} variant="primary" type="submit">
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default CloseApplicationModal;