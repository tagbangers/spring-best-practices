riot.render = function(tagName, opts) {
  var tag = riot.render.tag(tagName, opts),
    html = sdom.serialize(tag.root)
  // unmount the tag avoiding memory leaks
  tag.unmount()
  return html
}

riot.render.dom = function(tagName, opts) {
  return riot.render.tag(tagName, opts).root
}

riot.render.tag = function(tagName, opts) {
  var root = document.createElement(tagName),
    tag = riot.mount(root, opts)[0]
  return tag
}
