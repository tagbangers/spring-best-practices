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
				fetch('/todo', {method: 'post'}).then(function(response) {
					console.dir(response);
					todo.items.push({ title: todo.text })
					todo.text = todo.input.value = ''
				})
			}
		}

		removeAllDone(e) {
			todo.items = todo.items.filter(function(item) {
				return !item.done
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
			var item = e.item
			item.done = !item.done
			return true
		}
	</script>
</todo>
