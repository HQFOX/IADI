import React, {useEffect, useState} from "react";
import {Button, Form, Modal} from "react-bootstrap";
import {ReviewPost} from "../../types/Review.type";
import {Reviewer} from "../../types/Reviewer.type";
import {postData} from "../../Api";
import {useSelector} from "react-redux";
import {RootStore} from "../../Store";

const reviewer:Reviewer = {
    reviewerId: 0,
    name: ""
}
const initialReview:ReviewPost = {
    reviewId: 0,
    writtenReview: "",
    applicationId: 0,
    reviewerId: 0,
    evaluationPanelId: 0
}



const CreateReviewModal = (props:{show:boolean, handleShow:(value:boolean) => void, applicationId:number , evaluationPanelId:number}) => {

    const [review, setReview] = useState<ReviewPost>(initialReview)

    const login = useSelector((state: RootStore) => state.login);

    useEffect( () => {
        setReview( {
            ...review,
            evaluationPanelId: props.evaluationPanelId,
            applicationId: props.applicationId,
            reviewerId: login.currentId
        })
    } , [])

    function handleChange(event: { currentTarget: { name: any; value: any; }; })
    {
        const { name , value } = event.currentTarget;

        setReview({
            ...review,
            [name] : value
        })
    }

    function handleSubmit() {
        console.log(review)
        postData("review",review).then(
            () => props.handleShow(false)
        )
    }

    return (
        <Modal show={props.show} onHide={() => props.handleShow(false)}>
            <Modal.Header closeButton>
                <Modal.Title>New Review</Modal.Title>
            </Modal.Header>
            <Modal.Body>

                    <Form.Group controlId="exampleForm.ControlTextarea1">
                        <Form.Label><h5>Review:</h5></Form.Label>
                        <Form.Control name="writtenReview" onChange={handleChange} as="textarea" rows={3} value={review.writtenReview} />
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

export default CreateReviewModal;