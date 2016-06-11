<todo>
	<h3>{ opts.title }</h3>
	<ul>
		<li each={ items.filter(whatShow) }>
			<label class={ completed: done }>
				<input type="checkbox" checked={ done } onclick={ parent.toggle }> { title }
			</label>
		</li>
	</ul>
	<form onsubmit={ add }>
		<input name="input" onkeyup={ edit }>
		<button disabled={ !text }>Add #{ items.filter(whatShow).length + 1 }</button>
		<button disabled={ items.filter(onlyDone).length == 0 } onclick={ removeAllDone }>X{ items.filter(onlyDone).length } </button>
	</form>
	<script>
		var todo = this;
		todo.items = opts.items

		edit(e) {
			todo.text = e.target.value
		}

		add(e) {
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
		}

		removeAllDone(e) {
			fetch('/todo.json', {method: 'delete'}).then(function(response) {
				todo.items = todo.items.filter(function(item) {
					return !item.done
				})
				todo.update();
			})
		}

		// an two example how to filter items on the list
		whatShow(item) {
			return !item.hidden
		}

		onlyDone(item) {
			return item.done
		}

		toggle(e) {
			fetch('/todo/' + e.item.id + '.json', {method: 'put'}).then(function(response) {
				return response.json();
			}).then(function(json) {
				var item = e.item
				item.done = json.done
				todo.update();
				return true
			})
		}
	</script>
</todo>
