'use babel';

import React from 'react';
import { Grid, Row, Col, Navbar, Nav, NavItem } from 'react-bootstrap';
import MenuBar from '../components/MenuBar.jsx';
import ViewPane from '../components/ViewPane.jsx';
import NavPane from '../components/NavPane.jsx';
import Footer from '../components/Footer.jsx';

export default class Main extends React.Component {
  render() {
    return (
		<div id="wrapper">
			<MenuBar />
			<Grid id="main-grid" fluid={true}>
				<Row id="main-panes">
					<Col xs={3}><NavPane /></Col>
					<Col xs={9}><ViewPane /></Col>
				</Row>
				<Row id="footer">
					<Col xs={12}><Footer /></Col>
				</Row>
			</Grid>
		</div>
	)
  }
}
