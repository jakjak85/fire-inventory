var json = 
    {"city" : "Conshohocken",
	"firstName" : "John",
	"lastName" : "doe",
	"phoneNumber" : "321-654-1425",
	"emailAddress" : "213@abc.com",
	"streetAddress" : "34 street place",
	"state" : "CA",
	"zipCode" : "12345",
	"certificationsIds" : [ {"id":"1", "name": "FireFighter I"}, {"id":"2", "name": "FireFighter II"} ]};
	/*{"city" : "Conshohocken",
	"firstName" : "John",
	"lastName" : "doe2",
	"phoneNumber" : "321-654-1425",
	"emailAddress" : "213@abc.com",
	"streetAddress" : "34 street place",
	"state" : "CA",
	"zipCode" : "12345",
	"certificationsIds" : [ "123", "321" ]};*/

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
	render : function() {
		return (
				//TODO: add onchange event
				<input className={this.props.cName} value={this.props.data} type="text"/>
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
var Person = React.createClass({
	render : function() {
		return (<div>
				<FieldContainer cName="name" fName="Name" dName="divName" data={this.props.info.firstName + " " + this.props.info.lastName}/>
				<FieldContainer cName="phone" fName="Phone" dName="divPhone" data={this.props.info.phoneNumber}/>
				<FieldContainer cName="email" fName="Email" dName="divEmail" data={this.props.info.emailAddress}/>
				<FieldContainer cName="strAddr" fName="Street Address" dName="divStrAddr" data={this.props.info.streetAddress}/>
				<FieldContainer cName="city" fName="City" dName="divCity" data={this.props.info.city}/>
				<FieldContainer cName="state" fName="State" dName="divState" data={this.props.info.state}/>
				<FieldContainer cName="zip" fName="Zip" dName="divZip" data={this.props.info.phoneNumber}/>
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
	connection.send("{'firstName' : 'John', 'lastName' : 'Doe'}");
};





/*ReactDOM.render(<Person info={json}/>, document
		.getElementById('content'));*/