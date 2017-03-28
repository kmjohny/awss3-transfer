# A sample Guardfile
# More info at https://github.com/guard/guard#readme

guard :gradle, flags: '-Dspring.config.location=classpath:secret.yml' do
  watch(%r{^src/main/(.+)\.*$}) { |m| m[1].split('.')[0].split('/')[-1] }
  watch(%r{^src/test/(.+)\.*$}) { |m| m[1].split('.')[0].split('/')[-1] }
  watch('build.gradle')
end

notification :tmux,
  display_message: true,
  timeout: 5, # in seconds
  default_message_format: '%s >> %s',
  # the first %s will show the title, the second the message
  # Alternately you can also configure *success_message_format*,
  # *pending_message_format*, *failed_message_format*
  line_separator: ' > ', # since we are single line we need a separator
  # color_location: 'status-left-bg', # to customize which tmux element will change color
  color_location: [],

  # Other options:
  default_message_color: 'black',
  success: 'colour150',
  failure: 'colour174',
  pending: 'colour179',

  # Notify on all tmux clients
  display_on_all_clients: false
