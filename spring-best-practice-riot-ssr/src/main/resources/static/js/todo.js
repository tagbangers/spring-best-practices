riot.tag2('todo', '<h3>{opts.title}</h3> <ul> <li each="{items.filter(whatShow)}"> <label class="{completed: done}"> <input type="checkbox" __checked="{done}" onclick="{parent.toggle}"> {title} </label> </li> </ul> <form onsubmit="{add}"> <input name="input" onkeyup="{edit}"> <button __disabled="{!text}">Add #{items.filter(whatShow).length + 1}</button> <button __disabled="{items.filter(onlyDone).length == 0}" onclick="{removeAllDone}">X{items.filter(onlyDone).length} </button> </form>', '', '', function(opts) {
		var todo = this;
		todo.items = opts.items

		this.edit = function(e) {
			todo.text = e.target.value
		}.bind(this)

		this.add = function(e) {
			if (todo.text) {
				fetch('/todo', {method: 'post'}).then(function(response) {
					console.dir(response);
					todo.items.push({ title: todo.text })
					todo.text = todo.input.value = ''
				})
			}
		}.bind(this)

		this.removeAllDone = function(e) {
			todo.items = todo.items.filter(function(item) {
				return !item.done
			})
		}.bind(this)

		this.whatShow = function(item) {
			return !item.hidden
		}.bind(this)

		this.onlyDone = function(item) {
			return item.done
		}.bind(this)

		this.toggle = function(e) {
			var item = e.item
			item.done = !item.done
			return true
		}.bind(this)
});
