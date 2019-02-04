import React, {Component} from 'react'
import {Container, Row, Col} from 'reactstrap'
import Pane from '../Pane';
//import Units from './Options/Units'

/* Options allows the user to change the parameters for planning
 * and rendering the trip map and itinerary.
 * The options reside in the parent object so they may be shared with the Distance object.
 * Allows the user to set the options used by the application via a set of buttons.
 */
export default class About extends Component{
    constructor(props) {
        super(props);
    }


    render() {
        return(
            <Container>
                <Row>
            <   Col xs={12}>
                    {this.createHeader()}
                    </Col>
                </Row>
        <Row>
        <   Col  xs={12} sm={6}>
            {this.createJeremy()}
         </Col>

         <Col xs={12} sm={6}>
            {this.createDarien()}
        </Col>

        </Row>

        <Row>

        <Col xs={12} sm={6}>
            {this.createEdward()}
        </Col>

        <Col  xs={12} sm={6}>
            {this.createGriffin()}
        </Col>


        </Row>

            </Container>
        )
    }


    createHeader() {
        return (
            <Pane header={'About us'}
        bodyJSX={<div>We are a familiy friendly small buisiness trying to make a big impact in the world of trip management.
         We hope to change the way you plan trips online and for free! Below are the members of our team, who are dedicated
        to making this company a success.</div>}/>
    );
    }



    createJeremy() {
        return (
            <Pane header={'Jeremy Lesser'}
        bodyJSX={<div>  blurb to come </div>}/>
    );
    }

    createEdward() {
        return (
            <Pane header={'Edward Pike'}
        bodyJSX={<div>   blurb to come </div>}/>
    );
    }

    createDarien() {
        return (
            <Pane header={'Darien Cupit'}
        bodyJSX={<div> I am a third-year Computer Science student and AROTC cadet at Colorado State University. Born and raised in Colorado, I have spent the majority of my
            life visiting the mountains to hike, sight-see, and snowboard. Despite living in Colorado for my entire life, I have still managed to travel many locations both
            in and outside of the country such as Jamaica, Canada, Mexico, Thailand and the Bahamas. My work experience before college involved network administration at my
            family's business. While this wan't an arduous job, it is what got me interested in working on computers. When it came time to decided on a university, I decided
            to become a student at CSU due to its amazing Computer Science and ROTC programs, and have since gained many incredibly useful experiences and skills that will no
            doubt follow me for years to come.
        </div>}/>
    );
    }

    createGriffin() {
        return (
            <Pane header={'Griffin Gilbert'}
        bodyJSX={<div>  I am a third year computer science student at CSU. I am an Army ROTC
        cadet and a member of our school secruity club, Hashdump. Born in California and raised in Colorado
          I enjoy skiing, fishing, and playing sports. I am an avid reader and play too many video games for
            my own good.</div>}/>
    );
    }


}