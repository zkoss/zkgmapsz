import { ClientFunction } from 'testcafe';

fixture('Getting Started').page('http://localhost:8080/gmapszTest/test2/ZKGMAPS-3.zul');



test('My first test', async t => {
	
	const getZkUuidFromId= ClientFunction((id) => {
		return new Promise(resolve => {
			var uuid = zk.$('$'+id).uuid;
			console.log('found ' + uuid + ' from  ' + id);
			return "#"+uuid;
		});
	});
	
	console.log('start test');
	
	await t.click(getZkUuidFromId("mymark"));
	console.log('did click');
	await t.expect(Selector(await getZkUuidFromId("lbl")).innerText).eql("You've clicked on the marker.");
});