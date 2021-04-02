import React from "react";
import {Accordion, Button, Card, Container} from "react-bootstrap";
import {Review} from "../types/Review.type";

const ReviewsTable = (props:{reviews:Review[]}) => {

    return (
        <Container fluid>
            <h4>Reviews</h4>
            <Accordion>
                { props.reviews.map( review =>
                    <Card>
                        <Card.Header>
                            <Accordion.Toggle as={Button} variant="link" eventKey={review.reviewId.toString()}>
                                {review.reviewer.name}
                            </Accordion.Toggle>
                        </Card.Header>
                        <Accordion.Collapse eventKey={review.reviewId.toString()}>
                            <Card.Body>{review.writtenReview}</Card.Body>
                        </Accordion.Collapse>
                    </Card>
                )}
            </Accordion>
        </Container>
    )
}

export default ReviewsTable;