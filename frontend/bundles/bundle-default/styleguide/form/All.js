import plugins from '../PluginRegistry.js'
// import Form from './Form.js'
import EmailInput from './input/EmailInput.js'
import PasswordInput from './input/PasswordInput.js'
import TextInput from './input/TextInput.js'
import TextArea from './input/TextArea.js'

// plugins.register(Form, '.Form')
plugins.register(EmailInput, '.EmailInput')
plugins.register(PasswordInput, '.PasswordInput')
plugins.register(TextArea, '.TextArea')
plugins.register(TextInput, '.TextInput')
