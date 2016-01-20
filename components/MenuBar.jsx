'use babel';

import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem, Button } from 'react-bootstrap';

export default class MenuBar extends React.Component {
	render() {
		return (
			<Navbar id="main-nav" fixedTop={true} fluid={true} inverse={true}>
				<Nav>
					<NavDropdown title="File">
						<MenuItem></MenuItem>
						<MenuItem>Import</MenuItem>
						<MenuItem divider />
						<MenuItem>Close</MenuItem>
					</NavDropdown>
					<NavDropdown title="Edit">
					</NavDropdown>
				</Nav>
				<Nav pullRight>
					<NavItem>X</NavItem>
				</Nav>
			</Navbar>
		)
	}
}
