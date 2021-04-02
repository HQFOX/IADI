import * as React from 'react';
import {ChangeEvent, useState} from 'react';
import {Accordion, Button, Card, Form, Modal} from 'react-bootstrap';
import {MdDescription, MdTitle} from "react-icons/md";
import {
    AiOutlineSchedule,
    BiCheckSquare,
    ImAttachment,
    IoIosGitNetwork,
    RiFilePaper2Line,
    VscDebugStart
} from "react-icons/all";
import {useDispatch, useSelector} from "react-redux";
import {RootStore} from "../Store";
import {useHistory} from 'react-router-dom';
import {fullRequest, semiRequest} from "../utils/StudentAppUtil";

export interface Props {
    show: boolean;
    handleClose: () => void;
    grantTitle: string;
    grantDesc: string;
    dataitems: any[];
    grantId: number;
}

function NewAppModal({show, handleClose, grantTitle, grantDesc, dataitems, grantId}: Props) {
    const dispatch = useDispatch();
    const login = useSelector((state: RootStore) => state.login);
    const history = useHistory();

    const [intro, setIntro] = useState("");
    const [work, setWork] = useState("");
    const [related, setRelated] = useState("");
    const [pubs, setPubs] = useState("");

    //TODO
    const [file, setFile] = useState("");
    const [dataItemID, setdataItemID] = useState(0);
    const [reqFileSub, setReqFileSub] = useState(false);

    const submitForm = (state:string) => {
            submitApp(state);
    }

    const handleIntro = (e: ChangeEvent<HTMLTextAreaElement>) => setIntro(e.target.value);
    const handlePub = (e: ChangeEvent<HTMLTextAreaElement>) => setPubs(e.target.value);
    const handleRelated = (e: ChangeEvent<HTMLTextAreaElement>) => setRelated(e.target.value);
    const handleWork = (e: ChangeEvent<HTMLTextAreaElement>) => setWork(e.target.value);
    const handleFileInput = (e:any, item:any, mandatory:boolean) => {
        console.log(item);
        setdataItemID(item);
        console.log(e.target.files[0].name);
        setFile(e.target.files[0].name);
        if(mandatory)
            setReqFileSub(true);
    }
/*
        val appDataItemId: Long,
        val dataItemId: Long,
        val data: String
 */
    const submitApp = async (state:string) => {
        let request = (isCompleted() ? fullRequest(login.currentId, grantId, intro, related, work, pubs, state, dataItemID, file)
            : semiRequest(login.currentId, grantId, intro, related, work, pubs, state))
        fetch('/application', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        }).then(response => {
            if (response.ok) {
                handleClose();
                history.push('/myapplications');
            } else {
                console.log(response)
                window.alert(`Error: ${response.status}: ${response.statusText}`)
                return null;
            }
        })
            .catch(err => {
                console.log(err);
                return null
            })
    }

    const isCompleted = () => (intro != "" && work != "" && related != "" && pubs != "");

    return (
        <Modal centered size="lg" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Grant Call Application</Modal.Title>
            </Modal.Header>
            <Form>
                <Modal.Body>
                    <div><MdTitle className="icon"/>{grantTitle}</div>
                    <div><MdDescription className="icon"/>Description: <br/>
                        <textarea readOnly defaultValue={grantDesc}/></div>
                    <hr/>
                    <table width={"100%"}>
                        <tr>
                            <td>
                                <VscDebugStart className="icon"/>Introduction:
                                <textarea onChange={handleIntro}/>
                            </td>
                            <td>
                                <AiOutlineSchedule className="icon"/>Work Plan:
                                <textarea onChange={handleWork}/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <IoIosGitNetwork className="icon"/>Related Work:
                                <textarea onChange={handleRelated}/>
                            </td>
                            <td>
                                <RiFilePaper2Line className="icon"/>Publications:
                                <textarea onChange={handlePub}/>
                            </td>
                        </tr>
                    </table>
                    <hr/>
                    <Accordion>
                        {dataitems.map((item: any) => (
                            <Card>
                                <Card.Header>
                                    <Accordion.Toggle as={Button} variant="link" eventKey={item.dataItemId}>
                                        <ImAttachment/> {item.dataType} {item.mandatory == true ? (
                                        <div className="dataitem-mandatory">Mandatory Item <BiCheckSquare/>
                                        </div>) : (null)}
                                    </Accordion.Toggle>
                                </Card.Header>
                                <Accordion.Collapse eventKey={item.dataItemId}>
                                    <Card.Body>
                                        <div className="mb-3">
                                            <Form.File id="formcheck-api-regular">
                                                <Form.File.Label>Upload file</Form.File.Label>
                                                <Form.File.Input onChange={(e:any) => handleFileInput(e,item.dataItemId,item.mandatory)} />
                                            </Form.File>
                                        </div>
                                    </Card.Body>
                                </Accordion.Collapse>
                            </Card>
                        ))}
                    </Accordion>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="outline-success" onClick={() => submitForm("Temporary")}>
                        Save as Draft
                    </Button>
                    {isCompleted() == true ? (<Button variant="success" onClick={() => submitForm("Submitted")}>Submit Application</Button>)
                        : (<Button variant="success" disabled={true}>Submit Application</Button>)}

                </Modal.Footer>
            </Form>

        </Modal>
    )
}


export default NewAppModal;