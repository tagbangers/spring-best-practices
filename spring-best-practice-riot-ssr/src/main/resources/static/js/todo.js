riot.tag2('todo', '<h3>{opts.title}</h3> <ul> <li each="{items.filter(whatShow)}"> <label class="{completed: done}"> <input type="checkbox" __checked="{done}" onclick="{parent.toggle}"> {title} </label> </li> </ul> <form onsubmit="{add}"> <input name="input" onkeyup="{edit}"> <button __disabled="{!text}">Add #{items.filter(whatShow).length + 1}</button> <button __disabled="{items.filter(onlyDone).length == 0}" onclick="{removeAllDone}">X{items.filter(onlyDone).length} </button> </form>', '', '', function(opts) {
		var todo = this;
		todo.items = opts.items

		this.edit = function(e) {
			todo.text = e.target.value
		}.bind(this)

		this.add = function(e) {
			if (todo.text) {
				var data = new FormData()
				data.append('title', todo.text)

				fetch('/todo.json', {method: 'post', body: data}).then(function(response) {
					return response.json();
				}).then(function(json) {
					todo.items.push({ id: json.id, title: json.title })
					todo.text = todo.input.value = ''
					todo.update();
				})
			}
		}.bind(this)

		this.removeAllDone = function(e) {
			fetch('/todo.json', {method: 'delete'}).then(function(response) {
				todo.items = todo.items.filter(function(item) {
					return !item.done
				})
				todo.update();
			})
		}.bind(this)

		this.whatShow = function(item) {
			return !item.hidden
		}.bind(this)

		this.onlyDone = function(item) {
			return item.done
		}.bind(this)

		this.toggle = function(e) {
			fetch('/todo/' + e.item.id + '.json', {method: 'put'}).then(function(response) {
				return response.json();
			}).then(function(json) {
				var item = e.item
				item.done = json.done
				todo.update();
				return true
			})
		}.bind(this)
});
