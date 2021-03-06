# awss3-transfer
AWS s3 transfer.

### Initialize project using spring cli
`spring init -j=1.8 --package-name=org.jkm.awss3transfer -g=org.jkm.awss3transfer -a=awss3-transfer -n=awss3-transfer -d=integration -build=gradle -p=jar awss3-transfer`

### Configure guard-gradle
* Initialize guard gradle.  
`guard init gradle`
* Configure notifications by adding the following to the GuardFile.  
 
	```
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
	```