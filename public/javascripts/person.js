var Certifications = React.createClass({
	render : function() {
		var results = this.props.certs;
		return (<div className="certs"> 
					<label className="certTitle">Members Certifications</label>
					<ol>
			        {results.map(function(data) {
			          return <li key={data.id}>{data.name}</li>;
			        })}
			      </ol>
				</div>
				);
	}
});

var FieldName = React.createClass({
	render : function() {
		return (
		<label className={this.props.cName}>{this.props.fName + ': '}</label>		
		);
	}
});

var FieldValue = React.createClass({
	componentWillReceiveProps: function(nextProps) {
	    this.setState({value: nextProps.data});
	},
	getInitialState: function() {
	    return {value: this.props.data};
	  },
	handleChange: function(event) {
	    this.setState({value: event.target.value});
	  },
	render : function() {
		var value = this.state.value;
		return (
				<input className={this.props.cName} value={value} onChange={this.handleChange} type="text"/>
		);
	}
});

var FieldContainer = React.createClass({
		render : function() {
			return (<div className={this.props.dName}>
					<FieldName cName={this.props.cName} fName={this.props.fName}/>
					<FieldValue cName={this.props.cName + "Value"} data={this.props.data}/>
					</div>
			);
		}
	});

var ButtonClass = React.createClass({
	handleChange: function(event) {
		{this.props.onEvent(this.props.uId)}
	  },
    render: function(){
        return (
           <input type="button" className="btn btn-primary" onClick={this.handleChange} value={this.props.val} />
        )
    }
});


var updateAction = function(id) {
	connection.send(JSON.stringify(fieldsToJSON(id, "UPDATE")));
};

var addAction = function(id) {
	connection.send(JSON.stringify(fieldsToJSON("New User", "ADD")));
};

var removeAction = function(id) {
	var vals = {
			"id": id,
			"type" : "REMOVE"
	};
	connection.send(JSON.stringify(vals));
};

var queryAction = function()
{
	var searchParam = { "type" : "QUERY", 
						"firstName" : $('.qfnameValue').val(),
						"lastName" : $('.qlnameValue').val()
	}
	connection.send(JSON.stringify(searchParam));
}

var fieldsToJSON = function(id, type) {
	var vals = {
			"type" : type,
			"id": id,
			"city" : $('.cityValue').val(), 
			"firstName" : $('.fnameValue').val(),
			"lastName" : $('.lnameValue').val(),
			"phoneNumber" : $('.phoneValue').val(),
			"emailAddress" : $('.emailValue').val(),
			"streetAddress" : $('.strAddrValue').val(),
			"state" : $('.stateValue').val(),
			"zipCode" : $('.zipValue').val()
	};
	
	return vals;
}

var Person = React.createClass({
	render : function() {
		return (<div>
				<FieldContainer cName="qfname" fName="First Name" dName="divqFirstName" data={this.props.info.firstName}/>
				<FieldContainer cName="qlname" fName="Last Name" dName="divqLastName" data={this.props.info.lastName}/>
				<ButtonClass onEvent={queryAction} val="Query For User"/>
				<FieldContainer cName="fname" fName="First Name" dName="divFirstName" data={this.props.info.firstName}/>
				<FieldContainer cName="lname" fName="Last Name" dName="divLastName" data={this.props.info.lastName}/>
				<FieldContainer cName="phone" fName="Phone" dName="divPhone" data={this.props.info.phoneNumber}/>
				<FieldContainer cName="email" fName="Email" dName="divEmail" data={this.props.info.emailAddress}/>
				<FieldContainer cName="strAddr" fName="Street Address" dName="divStrAddr" data={this.props.info.streetAddress}/>
				<FieldContainer cName="city" fName="City" dName="divCity" data={this.props.info.city}/>
				<FieldContainer cName="state" fName="State" dName="divState" data={this.props.info.state}/>
				<FieldContainer cName="zip" fName="Zip" dName="divZip" data={this.props.info.phoneNumber}/>
				<ButtonClass onEvent={updateAction} uId={this.props.info.id} val="Update User"/>
				<ButtonClass onEvent={addAction} uId={this.props.info.id} val="Add As New User"/>
				<ButtonClass onEvent={removeAction} uId={this.props.info.id} val="Remove User"/>
				<Certifications certs={this.props.info.certificationsIds} />
				</div>
				);
	}
});
var url = jsRoutes.controllers.Application.socket();
var connection = new WebSocket(url.webSocketURL());


connection.onmessage = function (e) {
	var obj = JSON.parse(e.data);
	ReactDOM.render(<Person info={obj}/>, document.getElementById('content'));
};

connection.onopen = function () {
    var searchParam = { "type" : "QUERYID", "id" : "1"}
	connection.send(JSON.stringify(searchParam));
};