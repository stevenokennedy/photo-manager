'use babel';

import React from 'react';
import { Navbar, Nav, NavItem, NavDropdown, MenuItem, Button } from 'react-bootstrap';
import remote from 'remote';

export default class MenuBar extends React.Component {
	constructor(props) {
		super(props);
		this.window = remote.getCurrentWindow();
		this.handleWindowOperation = this.handleWindowOperation.bind(this);
	}
	
	render() 
	{
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
				<Nav pullRight onSelect={this.handleWindowOperation}>
					<NavItem eventKey={"min"}>_</NavItem>
					<NavItem eventKey={"max"}>[]</NavItem>
					<NavItem eventKey={"close"}>X</NavItem>
				</Nav>
			</Navbar>
		)
	}
	
	handleWindowOperation(op)
	{
		switch(op)
		{
			case "min": 
				this.window.minimize();
				break;
			case "max": 
				this.window.maximize();
				break;
			case "close":
				this.window.close();
				break;
		}
	}
}
